package com.example.tfm.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.tfm.model.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

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
