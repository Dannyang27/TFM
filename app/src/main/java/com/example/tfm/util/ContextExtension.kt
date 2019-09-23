package com.example.tfm.util

import android.content.Context
import android.content.Intent
import com.example.tfm.activity.ChatActivity
import com.example.tfm.activity.MainActivity

fun Context.launchMainActivity(){
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Context.launchChatActivity(id: String, user: String, fromActivity: Boolean){
    val intent = Intent(this, ChatActivity::class.java)

    if(!fromActivity){
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    intent.putExtra("conversationId", id)
    intent.putExtra("receiverEmail", user)
    startActivity(intent)
}