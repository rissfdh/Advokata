// index.js
const express = require('express');
const dotenv = require('dotenv');
const bodyParser = require('body-parser');
const authRoutes = require('./routes/authRoutes');
dotenv.config();

const app = express();

// Middleware untuk parsing JSON body
app.use(bodyParser.json());


// Rute untuk otentikasi
app.use('/api/auth', authRoutes);

// Rute utama untuk memverifikasi server berjalan
app.get('/', (req, res) => {
  res.send('Hello, World!');
});

// Menangani error global di Express
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ message: 'Something went wrong!' });
});

// Menentukan port untuk server
const PORT = process.env.PORT || 8080;

// Menjalankan server
app.listen(PORT, (err) => {
  if (err) {
    console.error('Failed to start server:', err);
    process.exit(1); // Keluar dengan status 1 jika ada error
  }
  console.log(`Server running on port ${PORT}`);
});
