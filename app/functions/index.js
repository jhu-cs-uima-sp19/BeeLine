let functions = require('firebase-functions');
let admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
function matchTokens(ids) {
    let ref = admin.database().ref("/tokens");
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            let tokens = snap.val();
            let tokenarr = [];
            for (let key of tokens) {
                if (ids.includes(key.key)) {
                    tokenarr.push(key.child().val());
                }
            }
            resolve(tokenarr);
        }, (err) => {
            reject(err);
        });
    });
    return defer;
}
exports.sendPush = functions.database.ref('/beelines/{zip}/{theBeeline}/participantIds}').onCreate((snapshot, context) => {
    const added_user = snapshot.val();
    return getUserName(user).then(name => {
        return loadUsers().then(userids => {
            let payload = {
                notification: {
                    title: 'Beeline',
                    body: name + " joined your Beeline!",
                    sound: 'default',
                    badge: '1'
                }
            };

            return matchTokens(userids).then(tokens => {
                return admin.messaging().sendToDevice(tokens, payload);
            });
        });
    });



    /*let projectStateChanged = false;
    let projectCreated = false;
    let projectData = event.data.val();
    if (!event.data.previous.exists()) {
        projectCreated = true;
    }
    if (!projectCreated && event.data.changed()) {
        projectStateChanged = true;
    }
    let msg = 'A project state was changed';
		if (projectCreated) {
			msg = `The following new project was added to the project: ${projectData.title}`;
		}
    return loadUsers().then(users => {
        let tokens = [];
        for (let user of users) {
            tokens.push(user.pushToken);
        }
        let payload = {
            notification: {
                title: 'Firebase Notification',
                body: msg,
                sound: 'default',
                badge: '1'
            }
        };
        return admin.messaging().sendToDevice(tokens, payload);
    });*/
});
function getUserName(id) {
    let db = admin.database().ref("/users/" + id + "/username");
    let defer = new Promise((resolve, reject) => {
        db.once('value', (snap) => {
            resolve(snap.val());
        }, (err) => {
            reject(err);
        });
    });
    return defer;
}
function getUsers(zip, beeline) {
    let dbRef = admin.database().ref('/beelines/' + zip + "/" + beeline + "/participantIds");
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            let userids = snap.val();
            let users = [];
            for (var index in userids) {
                users.push(userids[index]);
            }
            resolve(users);
        }, (err) => {
            reject(err);
        });
    });
    return defer;
}