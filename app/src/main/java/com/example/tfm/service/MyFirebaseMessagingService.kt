package com.example.tfm.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TFM", "New token generated: $token")
    }
}