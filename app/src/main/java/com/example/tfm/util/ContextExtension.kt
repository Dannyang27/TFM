package com.example.tfm.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.tfm.activity.ChatActivity
import com.example.tfm.activity.MainActivity

fun Context.launchMainActivity(){
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
}

fun Context.launchChatActivity(id: String, email: String, username: String, photo: String, fromActivity: Boolean){
    val intent = Intent(this, ChatActivity::class.java)

    if(!fromActivity){
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    intent.putExtra("conversationId", id)
    intent.putExtra("receiverEmail", email)
    intent.putExtra("receiverName", username)
    intent.putExtra("profilePhoto", photo)
    startActivity(intent)
}

fun Context.checkPermissions(permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}