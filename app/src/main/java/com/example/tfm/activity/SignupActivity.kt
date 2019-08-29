package com.example.tfm.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.example.tfm.model.User
import com.example.tfm.util.addUser
import com.example.tfm.util.trimBothSides
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var user : EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var joinusBtn: Button

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
        joinusBtn = findViewById(R.id.signup_joinus)

        joinusBtn.setOnClickListener {
            if(isFormNotEmpty()){
                disableViews()
                val user = User(email.text.toString().trimBothSides(), user.text.toString().trimBothSides(), "", "")
                addUserToFirestore(user)
            }else{
                toast("Form cannot be empty")
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

    private fun addUserToFirestore(user: User){
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnSuccessListener{
                FirebaseFirestore.getInstance().addUser(this, user)
                currentUserEmail = email.text.toString()
                currentUserPassword = password.text.toString()
            }
            .addOnFailureListener { enableViews() }
    }

    private fun isFormNotEmpty() = user.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()
    private fun disableViews(){
        user.isEnabled = false
        email.isEnabled = false
        password.isEnabled = false
        joinusBtn.isEnabled = false
    }
    private fun enableViews(){
        user.isEnabled = true
        email.isEnabled = true
        password.isEnabled = true
        joinusBtn.isEnabled = true
    }
}

