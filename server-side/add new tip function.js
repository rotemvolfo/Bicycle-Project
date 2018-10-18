const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


exports.addReviewsAboutThePlace = functions.https.onRequest((req, res) => {
    const userName = req.body.userName;
    const latitude = req.body.latitude;
    const longitude = req.body.longitude;
    const title = req.body.title;
    const text = req.body.text;
    admin.database().ref(`Reviews/${latitude},${longitude}/`).push({userName: userName, text: text, title: title});
    console.log("add reviews");
    res.sendStatus(200);
});
