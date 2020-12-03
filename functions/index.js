

const admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);
const db = admin.firestore();


exports.myFunction = functions.firestore
.document('event/{docId}')
   .onCreate((snap, context) => {
         newValue = snap.data()["eventUserId"];
  const userRef = db.collection('user/').doc(newValue);
  const res =  userRef.update({  NbCreateEventUser: admin.firestore.FieldValue.increment(1)});
return 0;
         } );

