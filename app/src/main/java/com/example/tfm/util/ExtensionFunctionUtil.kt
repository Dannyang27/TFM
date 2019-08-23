package com.example.tfm.util

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


fun DatabaseReference.loadFakeUsers(){
    val celia = User("celiasoler@gmail.com", "Celia Soler", "")
    this.child("Users").push().setValue(celia)
    val jorge = User("jorgeredon@gmail.com", "Jorge Redón", "")
    this.child("Users").push().setValue(jorge)
    val pam = User("pamtheoffice@gmail.com", "Pam Beesly", "")
    this.child("Users").push().setValue(pam)
    val lesley = User("weilesley@gmail.com", "Wei Lesley Yang", "")
    this.child("Users").push().setValue(lesley)
}

fun DatabaseReference.getAllUsers(){
    this.child("Users").addChildEventListener(object: ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, key: String?) {
            Log.d(LogUtil.TAG, "Email: ${dataSnapshot.getValue(User::class.java)?.email} has changed its value with key: $key")
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, key: String?) {
            Log.d(LogUtil.TAG, dataSnapshot.getValue(User::class.java)?.email)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }
    })
}