package com.example.tfm.util

import android.content.SharedPreferences
import android.view.View
import android.widget.GridView
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.adapter.EmojiGridViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun TextView.showUsernameIfGroup( isPrivateChat: Boolean, username: String){
    if(isPrivateChat) {
        this.visibility = View.GONE
    } else{
        this.text = username
        this.visibility = View.VISIBLE
    }
}

fun SharedPreferences.updateCurrentUser(email: String, password: String){
    val pref = edit()
    pref.putString("currentUserEmail", email.trimBothSides())
    pref.putString("currentUserPassword", password)
    pref.apply()
}

fun SharedPreferences.clearCredential(){
    val pref = edit()
    pref.putString("currentUserEmail", "")
    pref.putString("currentUserPassword", "")
    pref.apply()
}

fun SharedPreferences.getCredentials(): Pair<String, String>{
    val email = getString("currentUserEmail", "")
    val password = getString("currentUserPassword","")

    return Pair(email, password)
}

fun String.trimBothSides() = this.trimStart().trimEnd()

fun String.addCheck() = this.plus("✓")

fun RecyclerView.ViewHolder.setTime(time: TextView, timestamp: Long){
    time.text = TimeUtil.setTimeFromTimeStamp(timestamp)
}

fun RecyclerView.ViewHolder.setMessageCheckIfSeen(time: TextView, isSent: Boolean){
    if(isSent){
        time.text.toString().addCheck()
    }
}

suspend fun Fragment.loadGridview( gridview: GridView, emojiList: ArrayList<Int>){
    withContext(Dispatchers.IO) {
        val adapter = EmojiGridViewAdapter(activity?.applicationContext!!, emojiList)
        withContext(Dispatchers.Main) {
            gridview.adapter = adapter
        }
    }
}
