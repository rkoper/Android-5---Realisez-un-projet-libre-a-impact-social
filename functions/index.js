

const admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);

let db = admin.firestore();

exports.myFunction = functions.firestore
  .document('user/{docId}')
   .onUpdate((change, context) => {




         const newValue = change.after.data();
          eventNbEvent = change.before.data()["eventNbEvent"];
           DocIdUser = change.before.data()["DocIdUser"];
                console.log ('DocIdUser-----' + DocIdUser);
                        console.log ('eventNbEvent-----' + eventNbEvent);
    let updateap = db.collection('user').doc(DocIdUser).set({ eventNbEvent:eventNbEvent+1},{merge:true});

return 0;
         } );