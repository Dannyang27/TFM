package com.example.tfm.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import com.example.tfm.activity.MainActivity
import com.example.tfm.activity.UserSearcherActivity
import com.example.tfm.model.Conversation
import com.example.tfm.model.User
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

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

fun DatabaseReference.checkIfConversationExists( email: String){
    val ref = this.child("Users").orderByChild("email").startAt(AuthUtil.getAccountEmail()).limitToFirst(1)
    ref.addChildEventListener(object : ChildEventListener{
        override fun onCancelled(p0: DatabaseError) {}
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
        override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
        override fun onChildRemoved(p0: DataSnapshot) {}

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val user = dataSnapshot.getValue(User::class.java)
            Log.d(LogUtil.TAG, user?.email )
        }
    })
}

fun DatabaseReference.addPrivateChats(hashcode: String, conversation: Conversation){
    this.child("PrivateChats").child(hashcode).setValue(conversation)
}

fun DatabaseReference.addGroupChat(hashcode: String, conversation: Conversation){
    this.child("GroupChats").child(hashcode).setValue(conversation)
}

fun DatabaseReference.getAllUsers(){
    this.child("Users").addChildEventListener(object: ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
        override fun onChildRemoved(p0: DataSnapshot) {}

        override fun onChildChanged(dataSnapshot: DataSnapshot, key: String?) {
            Log.d(LogUtil.TAG, "Email: ${dataSnapshot.getValue(User::class.java)?.email} has changed its value with key: $key")
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, key: String?) {
            Log.d(LogUtil.TAG, dataSnapshot.getValue(User::class.java)?.email)
        }
    })
}

fun DatabaseReference.loadFakeUsers(){
    val danny = User("danny27995@gmail.com", "Le Danny Yang", "Haciendo el TFM", "dasdas")
    this.child("Users").child(danny.hashCode().toString()).setValue(danny)
    val celia = User("celiasoler@gmail.com", "Celia Soler", "Trabajando en el oysho jiji", "dasdas")
    this.child("Users").child(celia.hashCode().toString()).setValue(celia)
    val jorge = User("jorgeredon@gmail.com", "Jorge Redón", "Drakukeooo, te meto el dedoo", "dasda")
    this.child("Users").child(jorge.hashCode().toString()).setValue(jorge)
    val pam = User("pamtheoffice@gmail.com", "Pam Beesly", "Dunder Mifflin this is Pam", "dasdasasd")
    this.child("Users").child(pam.hashCode().toString()).setValue(pam)
    val lesley = User("weilesley@gmail.com", "Wei Lesley Yang", "Viajando...", "dasda")
    this.child("Users").child(lesley.hashCode().toString()).setValue(lesley)
}


fun FirebaseFirestore.addUser(context: Context, user: User){
    collection("users")
        .document(user.email)
        .set(User(user.email, user.name, user.status, user.profilePhoto))
        .addOnSuccessListener {
            Log.d(LogUtil.TAG, "User added into FirebaseFirestore")
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }.addOnFailureListener {
            Log.d(LogUtil.TAG, "Error adding document")
        }
}
