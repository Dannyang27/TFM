package com.example.tfm.util

import android.util.Log
import com.example.tfm.activity.UserSearcherActivity
import com.example.tfm.model.User
import com.google.firebase.database.*

object FirebaseUtil {
    val database = FirebaseDatabase.getInstance().reference
}

fun DatabaseReference.getUsersByName(name: String){
    if(name.isNotEmpty()){
        this.child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(LogUtil.TAG, databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                dataSnapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    user?.let {
                        if(it.name.contains(name, ignoreCase = true)){
                            users.add(it)
                        }
                    }
                }
                UserSearcherActivity.updateList(users)
            }
        })
    }
}

fun DatabaseReference.loadFakeUsers(){
    val celia = User("celiasoler@gmail.com", "Celia Soler", "Trabajando en el oysho jiji", "dasdas")
    this.child("Users").push().setValue(celia)
    val jorge = User("jorgeredon@gmail.com", "Jorge Redón", "Drakukeooo, te meto el dedoo", "dasda")
    this.child("Users").push().setValue(jorge)
    val pam = User("pamtheoffice@gmail.com", "Pam Beesly", "Dunder Mifflin this is Pam", "dasdasasd")
    this.child("Users").push().setValue(pam)
    val lesley = User("weilesley@gmail.com", "Wei Lesley Yang", "Viajando...", "dasda")
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
