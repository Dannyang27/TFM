package com.example.tfm.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.example.tfm.model.User
import com.example.tfm.util.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var user : EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var joinusBtn: Button
    private lateinit var prefs: SharedPreferences

    companion object{
        lateinit var currentUserEmail: String
        lateinit var currentUserPassword: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        toolbar = findViewById(R.id.signup_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = findViewById(R.id.signup_user)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        joinusBtn = findViewById<Button>(R.id.signup_joinus).apply {
            this.setOnClickListener {
                if(isFormNotEmpty()){
                    val user = User(email.text.toString(), user.text.toString(), "", "")
                    addUserToFirestore(user)
                }else{
                    toast("Form cannot be empty")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun getUser(email: String){
        val db = FirebaseFirestore.getInstance()

        val user = db.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                Log.d(LogUtil.TAG, user?.toString())
            }.addOnFailureListener { exception ->
                Log.w(LogUtil.TAG, "Error getting documents.", exception)
            }
    }

    private fun addUserToFirestore(user: User){
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnSuccessListener(object : OnSuccessListener<AuthResult>{
                override fun onSuccess(p0: AuthResult?) {
                    FirebaseFirestore.getInstance().addUser(applicationContext, user)
                    currentUserEmail = email.text.toString()
                    currentUserPassword = password.text.toString()
                }
            })
    }

    private fun isFormNotEmpty() = user.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()
}

