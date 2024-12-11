const mysql = require('mysql2');
require('dotenv').config();


const dbConfig = {
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
};

// Tentukan socketPath jika host adalah socket Cloud SQL
if (process.env.DB_HOST.includes('/cloudsql/')) {
  dbConfig.socketPath = process.env.DB_HOST;
} else {
  dbConfig.host = process.env.DB_HOST;
}

const db = mysql.createConnection(dbConfig);

db.connect((err) => {
  if (err) {
    console.error('Unable to connect to the database:', err);
    process.exit(1);
  } else {
    console.log('Database connected successfully.');
  }
});

module.exports = db;