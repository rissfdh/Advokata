global.self = global; 

require('dotenv').config();
const tf = require('@tensorflow/tfjs-node');
const { downloadModelFilesFromGCS, downloadTokenizerFileFromGCS, downloadEmbeddingsFileFromGCS, db } = require('../config/googleConfig');
const fs = require('fs').promises;
const admin = require('firebase-admin');
const cosineSimilarity = require('cosine-similarity');

let model;
let tokenizer;
let embeddings;

const loadTokenizer = async () => {
  try {
    const localTokenizerPath = await downloadTokenizerFileFromGCS('advokata-model/tokenizer/tokenizer.json');
    console.log('Tokenizer downloaded to:', localTokenizerPath);

    const tokenizerData = await fs.readFile(localTokenizerPath, 'utf8');
    tokenizer = JSON.parse(tokenizerData);
    console.log('Tokenizer loaded successfully');
  } catch (error) {
    console.error('Error loading the tokenizer from GCS:', error);
    throw new Error('Tokenizer loading failed');
  }
};


const loadEmbeddings = async () => {
  try {
    console.log('Downloading embeddings from GCS...');
    const localEmbeddingsPath = await downloadEmbeddingsFileFromGCS('advokata-model/embeddings/embeddings.json');
    console.log('Embeddings downloaded to:', localEmbeddingsPath);

    const embeddingsData = await fs.readFile(localEmbeddingsPath, 'utf8');
    embeddings = JSON.parse(embeddingsData);
    console.log('Embeddings loaded successfully:', embeddings);
    return embeddings;
  } catch (error) {
    console.error('Error loading the embeddings from GCS:', error);
    throw new Error('Embeddings loading failed');
  }
};

const loadModel = async () => {
  try {
    console.log('Downloading model from GCS...');
    // Unduh file model.json dan file shard seperti model-shard2
    const localModelJsonPath = await downloadModelFilesFromGCS('advokata-model/tfjs-model');
    console.log('Model JSON downloaded to:', localModelJsonPath);

    const modelUrl = `file://${localModelJsonPath}`;
    model = await tf.loadGraphModel(modelUrl);
    console.log('Model loaded successfully');
    return model;
  } catch (error) {
    console.error('Error loading the model from GCS:', error);
    throw new Error('Model loading failed');
  }
};


const tokenizeQuestion = (question) => {
  console.log('Original Question:', question);

  const tokens = question.split(' ').map((word) => {
    const token = tokenizer[word] || tokenizer['<unk>'] || 0;
    console.log(`Word: "${word}", Token: ${token}`);
    return token;
  });

  const tokenTensor = tf.tensor2d([tokens], [1, tokens.length], 'int32'); 

  const desiredLength = 768; 
  if (tokenTensor.shape[1] < desiredLength) {
    const padding = tf.zeros([1, desiredLength - tokenTensor.shape[1]], 'int32');
    tokenTensor.concat(padding, 1);
  }

  console.log('Tokenized Question (Tensor):', tokenTensor.toString());

  return tokenTensor;
};


const calculateCosineSimilarity = async (a, b) => {
  try {
    const a1D = a.squeeze();
    const b1D = b.squeeze();

    const maxLength = Math.max(a1D.shape[0], b1D.shape[0]);
    let paddedA = tf.pad(a1D, [[0, maxLength - a1D.shape[0]]]).toFloat();
    let paddedB = tf.pad(b1D, [[0, maxLength - b1D.shape[0]]]).toFloat();

    console.log('Padded A Shape:', paddedA.shape);
    console.log('Padded B Shape:', paddedB.shape);

    const dotProduct = await paddedA.dot(paddedB).data();
    const normA = await paddedA.norm().data();
    const normB = await paddedB.norm().data();

    if (normA[0] === 0 || normB[0] === 0) {
      console.warn('One of the vectors has zero norm. Returning similarity as 0.');
      return 0;
    }

    const similarity = dotProduct[0] / (normA[0] * normB[0]);

    tf.dispose([a1D, b1D, paddedA, paddedB]);

    return similarity;
  } catch (error) {
    console.error('Error in calculateCosineSimilarity:', error);
    throw error;
  }
};




const searchContext = async (tokenizedQuestion) => {
  try {
    console.log('Searching for context for tokenized question:', tokenizedQuestion);

    if (!Array.isArray(embeddings)) {
      throw new Error('Embeddings should be an array');
    }

    let bestMatch = null;
    let highestSimilarity = -1;

    const tokenizedTensor1D = tokenizedQuestion.squeeze();

    for (const embedding of embeddings) {
      const embeddingVector = Array.isArray(embedding.tokens) ? embedding.tokens : embedding.vector;

      if (!embeddingVector || !Array.isArray(embeddingVector)) {
        console.error('Invalid embedding:', embedding);
        continue;
      }

      const embeddingTensor = tf.tensor(embeddingVector).squeeze();

      try {
        const similarity = await calculateCosineSimilarity(tokenizedTensor1D, embeddingTensor);
        console.log(`Similarity with context "${embedding.context}":`, similarity);

        if (similarity > highestSimilarity) {
          highestSimilarity = similarity;
          bestMatch = embedding;
        }
      } catch (similarityError) {
        console.error('Error calculating similarity:', similarityError);
      } finally {
        tf.dispose(embeddingTensor);
      }
    }

    tf.dispose(tokenizedTensor1D);

    if (bestMatch) {
      console.log('Best match found:', bestMatch.context);
      return bestMatch.context;
    } else {
      console.warn('No relevant context found. Returning fallback context.');
      return 'Fallback context: Tidak ada konteks yang cocok ditemukan.';
    }
  } catch (error) {
    console.error('Error during context search:', error);
    throw new Error('Context search failed');
  }
};




const saveToFirestore = async (question, answer) => {
  try {
    await db.collection('qa_logs').add({
      question,
      answer,
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
    });
    console.log('Saved to Firestore successfully');
  } catch (error) {
    console.error('Error saving to Firestore:', error);
  }
};


const getAnswer = async (question) => {
  if (!model) {
    throw new Error('Model is not loaded yet');
  }

  if (!tokenizer) {
    throw new Error('Tokenizer is not loaded yet');
  }

  try {
    // Tokenize the question
    const tokenizedQuestion = tokenizeQuestion(question);
    console.log('Tokenized Question:', tokenizedQuestion);

    // Search for relevant context
    const context = await searchContext(tokenizedQuestion);
    console.log('Context:', context);

    if (!context) {
      throw new Error('No relevant context found');
    }

    // Tokenize the context
    const tokenizedContext = tokenizeQuestion(context);
    console.log('Tokenized Context:', tokenizedContext);

    // Define maximum length supported by the model
    const maxLength = 512;

    // Truncate question and context tokens if they exceed max length
    // In your `getAnswer` function, use this truncation method
    const truncatedTokenizedQuestion = truncateTensorToLength(tokenizedQuestion, 33);  // Padding/truncating to 33 tokens
    const truncatedTokenizedContext = truncateTensorToLength(tokenizedContext, 33);    // Padding/truncating to 33 tokens


    // Calculate the maximum length of both the question and context
    const inputLength = Math.max(
      truncatedTokenizedQuestion.shape[1],
      truncatedTokenizedContext.shape[1]
    );

    // Create attention mask and token type IDs with the appropriate input length
    const attentionMask = tf.ones([1, inputLength], 'int32');
    const tokenTypeIds = tf.zeros([1, inputLength], 'int32');

    // Prepare the input tensor for the model
    const inputTensor = {
      'input_ids': truncatedTokenizedQuestion,
      'attention_mask': attentionMask,
      'token_type_ids': tokenTypeIds
    };

    console.log('Input Tensor:', inputTensor);

    // Make the prediction using the model
    const prediction = await model.execute(inputTensor);

    // Ensure prediction is a Tensor
    if (!(prediction instanceof tf.Tensor)) {
      console.error('Prediction is not a Tensor:', prediction);
      throw new Error('Prediction is not a Tensor');
    }

    // Retrieve the output from the prediction tensor
    const answerData = await prediction.array();
    console.log('Prediction Output:', answerData);

    // Convert the answer data to a string (assuming it's a tokenized answer)
    const answerText = answerData.join(' ').trim(); // Join array elements into a string
    console.log('Answer:', answerText);

    // Save the question and answer to Firestore
    await saveToFirestore(question, answerText);

    return answerText;
  } catch (error) {
    console.error('Error during model prediction:', error.message);
    throw new Error(`Model prediction failed: ${error.message}`);
  }
};



const truncateTensorToLength = (tensor, maxLength) => {
  const currentLength = tensor.shape[1];
  if (currentLength > maxLength) {
    tensor = tensor.slice([0, 0], [1, maxLength]);  // Truncate tensor if too long
  } else if (currentLength < maxLength) {
    const padding = tf.zeros([1, maxLength - currentLength], 'int32');
    tensor = tensor.concat(padding, 1);  // Pad tensor if too short
  }
  return tensor;
};








module.exports = { loadModel, getAnswer, loadTokenizer, loadEmbeddings };
