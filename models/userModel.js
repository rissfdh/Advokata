const db = require('../config/db'); // Your DB connection setup
require('dotenv').config();


// Create a new user
const createUser = (name, email, password, callback) => {
  const query = 'INSERT INTO users (name, email, password) VALUES (?, ?, ?)';
  db.query(query, [name, email, password], (err, result) => {
    if (err) {
      console.error('Error inserting user:', err);
      return callback(err);
    }

    if (email.endsWith('@lawyer.com')) {
      const userId = result.insertId;

      const checkLawyerQuery = 'SELECT * FROM lawyers WHERE user_id = ?';
      db.query(checkLawyerQuery, [userId], (err, lawyerResult) => {
        if (err) {
          console.error('Error checking lawyer:', err);
          return callback(err);
        }

        if (lawyerResult.length > 0) {
          console.log('Lawyer already exists for this user_id');
          return callback(null, result);  // Don't insert again if already exists
        }

        const lawyerQuery = 'INSERT INTO lawyers (user_id, name, specialization, ktpa, ratings, experience_years, contact, availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
        const lawyerData = [userId, name, 'Unknown', 'Unknown', 0, 0, email, true];

        db.query(lawyerQuery, lawyerData, (err, lawyerResult) => {
          if (err) {
            console.error('Error inserting into lawyers:', err);
            return callback(err);
          }
          callback(null, result);  // Return user result after inserting into lawyers
        });
      });
    } else {
      callback(null, result);  // Return user result if not a lawyer
    }
  });
};

// Find user by email
const findUserByEmail = (email, callback) => {
  const query = 'SELECT * FROM users WHERE email = ?';
  db.query(query, [email], (err, result) => {
    if (err) {
      console.error('Error finding user:', err);
      return callback(err);
    }
    callback(null, result[0]); // Return the first result
  });
};

// Update profile picture URL
const updateUserProfilePicture = (userId, photoUrl, callback) => {
  const query = 'UPDATE users SET photoUrl = ? WHERE id = ?';
  db.query(query, [photoUrl, userId], (err, result) => {
    if (err) {
      console.error('Error updating profile picture:', err);
      return callback(err);
    }
    callback(null, result); // Return result of the update
  });
};

module.exports = { createUser, findUserByEmail, updateUserProfilePicture };