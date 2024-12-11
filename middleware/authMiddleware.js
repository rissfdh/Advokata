const jwt = require('jsonwebtoken');

const authMiddleware = (req, res, next) => {
  // Get the token from the Authorization header
  const token = req.headers['authorization']?.split(' ')[1]; // Assuming the format is 'Bearer <token>'

  if (!token) {
    return res.status(401).json({ error: true, message: 'No token provided' });
  }

  // Verify the token using jwt.verify()
  jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
    if (err) {
      return res.status(401).json({ error: true, message: 'Invalid or expired token' });
    }

    // If token is valid, store decoded user data in req.user
    req.user = decoded; 
    next(); // Proceed to the next middleware or route handler
  });
};

module.exports = authMiddleware;