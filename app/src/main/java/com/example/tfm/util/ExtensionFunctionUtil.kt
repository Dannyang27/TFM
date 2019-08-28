package com.example.tfm.util

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView

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
    pref.putString("currentUserEmail", email)
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
