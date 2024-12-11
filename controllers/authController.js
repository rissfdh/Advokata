const { Storage } = require('@google-cloud/storage');
const multer = require('multer');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { createUser, findUserByEmail, updateUserProfilePicture } = require('../models/userModel');
require('dotenv').config(); // Load environment variables

// Initialize Google Cloud Storage
const storage = new Storage();
const bucket = storage.bucket(process.env.GCS_BUCKET_NAME);

// Setup multer to handle file uploads
const upload = multer({
  storage: multer.memoryStorage(), // Store file in memory
  limits: { fileSize: 5 * 1024 * 1024 }, // Limit file size to 5MB
});

// Register User
const register = async (req, res) => {
  const { name, email, password } = req.body;

  try {
    // Hash password before saving
    const hashedPassword = await bcrypt.hash(password, 10);

    // Save new user to the database
    createUser(name, email, hashedPassword, (err, result) => {
      if (err) {
        return res.status(500).json({ error: true, message: 'Error registering user' });
      }
      res.status(201).json({ error: false, message: 'User registered successfully' });
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Error registering user' });
  }
};

// Login function
const login = async (req, res) => {
  const { email, password } = req.body;

  try {
    findUserByEmail(email, async (err, user) => {
      if (err || !user) {
        return res.status(400).json({ error: true, message: 'User not found' });
      }

      const isMatch = await bcrypt.compare(password, user.password);
      if (!isMatch) {
        return res.status(400).json({ error: true, message: 'Invalid credentials' });
      }

      const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });

      res.json({
        error: false,
        message: 'Login successful!',
        loginResult: {
          userId: user.id,
          name: user.name,
          token,
          photoUrl: user.photoUrl, // Add photoUrl to response
        },
      });
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Error logging in' });
  }
};

// Update Profile Picture
const updateProfilePicture = async (req, res) => {
  const userId = req.user.id; // Assuming the user is authenticated and user ID is available

  try {
    // Check if file is provided
    if (!req.file) {
      return res.status(400).json({ error: true, message: 'Profile photo is required' });
    }

    // Upload profile photo to Cloud Storage
    const file = req.file;
    const fileName = `${Date.now()}-${file.originalname}`;
    const fileUpload = bucket.file(fileName);

    const blobStream = fileUpload.createWriteStream({
      metadata: {
        contentType: file.mimetype,
      },
    });

    blobStream.on('error', (err) => {
      console.error(err);
      return res.status(500).json({ error: true, message: 'Error uploading photo' });
    });

    blobStream.on('finish', async () => {
      // Get the file's public URL
      const photoUrl = `https://storage.googleapis.com/${bucket.name}/${fileName}`;

      // Update user's profile with new photo URL
      updateUserProfilePicture(userId, photoUrl, (err, result) => {
        if (err) {
          return res.status(500).json({ error: true, message: 'Error updating profile picture' });
        }
        res.status(200).json({
          error: false,
          message: 'Profile picture updated successfully',
          photoUrl, // Return new photo URL in response
        });
      });
    });

    blobStream.end(file.buffer);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Error updating profile picture' });
  }
};

// Logout function
const logout = (req, res) => {
  res.json({ error: false, message: 'Logged out successfully' });
};

module.exports = { register, login, logout, updateProfilePicture, upload };