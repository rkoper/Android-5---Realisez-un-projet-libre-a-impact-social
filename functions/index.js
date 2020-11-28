

const admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);
const db = admin.firestore();


exports.myFunction = functions.firestore
.document('event/{docId}')
   .onCreate((snap, context) => {
         newValue = snap.data()["eventUserId"];
  const userRef = db.collection('user/').doc(newValue);
  const res =  userRef.update({  eventNbEvent: admin.firestore.FieldValue.increment(1)});
return 0;
         } );

exports.myFunction2 = functions.firestore
     .document('user/{docId}')
       .onUpdate((change, context) => {

              PhotoUserAfter = change.after.data()["PhotoUser"];
               DocIdUser = change.before.data()["DocIdUser"];

     const citiesRef = db.collection('event');
     const snapshot = citiesRef.where('eventUserId', '==', DocIdUser).get().then((snapshot)=>{

        if (snapshot.empty)
        { console.log('No matching documents.');
            return; }

          snapshot.forEach(doc => {
          db.collection("event").doc(doc.id).update({  eventUserPhoto: PhotoUserAfter});
           return ;});
              return ; });





     const itemRef = db.collection('item');
     const itemShot = itemRef.where('eventUserId', '==', DocIdUser).get().then((snapshot)=>{

     if (itemShot.empty)
     { console.log('No matching documents.');
       return; }

     itemShot.forEach(docu => {
     db.collection("event").doc(doc.id).update({  eventUserPhoto: PhotoUserAfter});
     return ;});
                        return ; });


  return ;});

