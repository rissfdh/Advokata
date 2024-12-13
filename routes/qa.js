// routes/qa.js
global.self = global; // Polyfill untuk self

const express = require('express');
const router = express.Router();
const { getAnswer } = require('../services/qaService');
require('dotenv').config();

// Endpoint untuk menerima pertanyaan dan mengembalikan jawaban
router.post('/predict', async (req, res) => {
  try {
    const { question } = req.body; // Menerima pertanyaan dari body

    // Validasi input
    if (!question) {
      return res.status(400).json({ error: 'Question is required' });
    }

    // Mendapatkan jawaban berdasarkan pertanyaan
    const answer = await getAnswer(question);

    // Mengembalikan jawaban dalam bentuk JSON
    return res.json({ answer });
  } catch (error) {
    console.error('Error processing the request:', error);
    return res.status(500).json({ error: 'Internal Server Error', message: error.message });
  }
});

module.exports = router;
