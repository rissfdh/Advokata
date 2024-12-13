import numpy as np
import json
import tensorflow as tf
from transformers import TFBertForQuestionAnswering, BertTokenizerFast
from sentence_transformers import SentenceTransformer, util

# **Konfigurasi**
qa_model_path = "data/saved_model/"
tokenizer_path = "data/tokenizer/"
embeddings_path = "data/embeddings2.json"
model_name = "paraphrase-multilingual-mpnet-base-v2"
cosine_similarity_threshold = 0.7

# **Load Model dan Tokenizer**
qa_model = TFBertForQuestionAnswering.from_pretrained(qa_model_path)
tokenizer = BertTokenizerFast.from_pretrained(tokenizer_path)
sentence_model = SentenceTransformer(model_name, device="cpu")

# **Load Embeddings**
with open(embeddings_path, "r") as f:
    embeddings_data = json.load(f)

questions = [entry["question"] for entry in embeddings_data]
contexts = [entry["context"] for entry in embeddings_data]
embeddings = [sentence_model.encode(question, convert_to_tensor=False) for question in questions]

# **Generate Answer**
def predict_answer(question, context, model, tokenizer, max_seq_length=512):
    inputs = tokenizer.encode_plus(
        question,
        context,
        add_special_tokens=True,
        max_length=max_seq_length,
        truncation=True,
        padding="longest",
        return_tensors="tf"
    )
    outputs = model(**inputs)
    start_logits, end_logits = outputs.start_logits, outputs.end_logits

    start_index = tf.argmax(start_logits, axis=1).numpy()[0]
    end_index = tf.argmax(end_logits, axis=1).numpy()[0]

    if start_index >= end_index:
        return "Maaf, saya tidak menemukan jawaban yang relevan."

    return tokenizer.decode(inputs['input_ids'][0][start_index:end_index+1], skip_special_tokens=True)

# **Interactive CLI**
print("Chatbot KUHP siap! Ketik 'exit' untuk keluar.")
while True:
    user_input = input("Anda: ")
    if user_input.lower() == 'exit':
        print("Terima kasih! Sampai jumpa.")
        break

    # Hitung kemiripan embeddings
    input_embedding = sentence_model.encode(user_input, convert_to_tensor=False)
    similarities = [util.cos_sim(input_embedding, embedding).item() for embedding in embeddings]

    # Cari konteks terbaik
    max_similarity = max(similarities)
    if max_similarity < cosine_similarity_threshold:
        print("Chatbot KUHP: Maaf, saya tidak menemukan konteks yang relevan untuk pertanyaan Anda.")
        continue

    best_match_idx = np.argmax(similarities)
    context = contexts[best_match_idx]
    print(f"Konteks Terpilih: {context}")  # Debug log

    # Prediksi jawaban
    answer = predict_answer(user_input, context, qa_model, tokenizer)
    print(f"Chatbot KUHP: {answer}")
