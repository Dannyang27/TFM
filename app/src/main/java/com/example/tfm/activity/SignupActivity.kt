package com.example.tfm.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.model.User
import com.example.tfm.util.addUser
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.example.tfm.util.trimBothSides
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    companion object{
        lateinit var currentUserEmail: String
        lateinit var currentUserPassword: String
        lateinit var progressbar: ProgressBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup_toolbar.title = ""
        setSupportActionBar(signup_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressbar = findViewById(R.id.signup_progressbar)

        signup_joinus.setOnClickListener {
            if(isFormNotEmpty()){
                progressbar.start()
                disableViews()
                val user = User("", signup_email.text.toString().trimBothSides(), signup_user.text.toString().trimBothSides(), "", "")
                val hashcode = user.hashCode().toString()
                user.id = hashcode

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
        auth.createUserWithEmailAndPassword(signup_email.text.toString(), signup_password.text.toString())
            .addOnSuccessListener{
                FirebaseFirestore.getInstance().addUser(this, user)
                currentUserEmail = signup_email.text.toString()
                currentUserPassword = signup_password.text.toString()
            }
            .addOnFailureListener {
                enableViews()
                progressbar.stop()
                toast("Email already exist or badly formatted")
            }
    }

    private fun isFormNotEmpty() = signup_user.text.isNotEmpty() && signup_email.text.isNotEmpty() && signup_password.text.isNotEmpty()
    private fun disableViews(){
        signup_user.isEnabled = false
        signup_email.isEnabled = false
        signup_password.isEnabled = false
        signup_joinus.isEnabled = false
    }
    private fun enableViews(){
        signup_user.isEnabled = true
        signup_email.isEnabled = true
        signup_password.isEnabled = true
        signup_joinus.isEnabled = true
    }
}

