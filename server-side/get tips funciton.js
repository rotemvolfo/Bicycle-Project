const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
var db = admin.database();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.getReviews = functions.https.onRequest((req, res) => {
 
  const latitude = req.body.latitude;
  const   longitude = req.body.longitude;
  let ref = db.ref(`Reviews/${latitude},${longitude}`);
    ref.on('value', (snapshot) => {
		res.status(200).json(snapshot.val())      
    });
});

