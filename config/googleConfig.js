const { Storage } = require('@google-cloud/storage');
const path = require('path');
const fs = require('fs').promises;

const storage = new Storage();

/**
 * Fungsi untuk mengunduh file tokenizer dari GCS.
 * @param {string} tokenizerFilePath - Path file tokenizer di GCS.
 * @returns {string} Path lokal ke file tokenizer.json.
 */
const downloadTokenizerFileFromGCS = async (tokenizerFilePath) => {
  const bucketName = process.env.GCS_BUCKET_NAME;
  const destinationDir = '/tmp/tokenizer'; // Direktori lokal untuk menyimpan tokenizer

  try {
    // Pastikan direktori tujuan ada
    await fs.mkdir(destinationDir, { recursive: true });

    console.log(`Downloading tokenizer file from gs://${bucketName}/${tokenizerFilePath}...`);

    // Ambil file tokenizer dari GCS
    const file = storage.bucket(bucketName).file(tokenizerFilePath);
    const destination = path.resolve(destinationDir, path.basename(tokenizerFilePath));

    await file.download({ destination });
    console.log(`Downloaded tokenizer to: ${destination}`);

    return destination;
  } catch (error) {
    console.error('Error downloading tokenizer file from GCS:', error.message);
    throw new Error(`Failed to download tokenizer file: ${error.message}`);
  }
};


/**
 * Fungsi untuk mengunduh file embeddings dari GCS.
 * @param {string} embeddingsFilePath - Path file embeddings di GCS.
 * @returns {string} Path lokal ke file embeddings.json.
 */
const downloadEmbeddingsFileFromGCS = async (embeddingsFilePath) => {
  const bucketName = process.env.GCS_BUCKET_NAME;
  const destinationDir = '/tmp/embeddings'; // Direktori lokal untuk menyimpan embeddings

  try {
    // Pastikan direktori tujuan ada
    await fs.mkdir(destinationDir, { recursive: true });

    console.log(`Downloading embeddings file from gs://${bucketName}/${embeddingsFilePath}...`);

    // Ambil file embeddings dari GCS
    const file = storage.bucket(bucketName).file(embeddingsFilePath);
    const destination = path.resolve(destinationDir, path.basename(embeddingsFilePath));

    await file.download({ destination });
    console.log(`Downloaded embeddings to: ${destination}`);

    return destination;
  } catch (error) {
    console.error('Error downloading embeddings file from GCS:', error.message);
    throw new Error(`Failed to download embeddings file: ${error.message}`);
  }
};


/**
 * Fungsi untuk mengunduh file model dari GCS.
 * @param {string} modelDirPath - Path direktori model di GCS.
 * @returns {string} Path lokal ke file model.json.
 */
const downloadModelFilesFromGCS = async (modelDirPath) => {
  const bucketName = process.env.GCS_BUCKET_NAME;
  const destinationDir = '/tmp/model'; // Direktori lokal untuk menyimpan model

  try {
    // Pastikan direktori tujuan ada
    await fs.mkdir(destinationDir, { recursive: true });

    console.log(`Downloading model files from gs://${bucketName}/${modelDirPath}...`);

    // Dapatkan daftar semua file dalam direktori model
    const [files] = await storage.bucket(bucketName).getFiles({ prefix: modelDirPath });
    
    // Unduh semua file
    for (const file of files) {
      const destination = path.resolve(destinationDir, path.basename(file.name));
      await file.download({ destination });
      console.log(`Downloaded: ${file.name} -> ${destination}`);
    }

    console.log('All model files downloaded successfully');
    return path.resolve(destinationDir, 'model.json');
  } catch (error) {
    console.error('Error downloading model files from GCS:', error.message);
    throw new Error(`Failed to download model files: ${error.message}`);
  }
};

module.exports = { downloadModelFilesFromGCS, downloadTokenizerFileFromGCS, downloadEmbeddingsFileFromGCS };
