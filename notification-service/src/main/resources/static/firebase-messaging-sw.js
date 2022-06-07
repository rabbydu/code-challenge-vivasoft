importScripts('https://www.gstatic.com/firebasejs/7.14.6/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.14.6/firebase-messaging.js');

var firebaseConfig = {
    apiKey: "AIzaSyAeMyG_NC8Xaay-YzyIPVJbFGrd0BdZcPU",
    authDomain: "notification-service-7b6a3.firebaseapp.com",
    projectId: "notification-service-7b6a3",
    storageBucket: "notification-service-7b6a3.appspot.com",
    messagingSenderId: "57256990423",
    appId: "1:57256990423:web:c49987eadc5a73ec2ec292",
    measurementId: "G-C92MRS6T2Z"
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function (payload) {
    console.log(payload);
    const notification = JSON.parse(payload);
    const notificationOption = {
        body: notification.body,
        icon: notification.icon
    };
    return self.registration.showNotification(payload.notification.title, notificationOption);
});