package com.example.tfm.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.util.LogUtil
import com.example.tfm.util.getCredentials
import com.example.tfm.util.updateCurrentUser
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var prefs: SharedPreferences
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        auth?.let {
            Log.d(LogUtil.TAG, "CurrentUserEmail: " + it.currentUser?.email)
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val (emailPref, passwordPref) = prefs.getCredentials()

        if(emailPref.isNotEmpty() && passwordPref.isNotEmpty()){
            loginUser(emailPref, passwordPref)
        }

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)

        signupBtn = findViewById<Button>(R.id.login_signup_button).apply {
            setOnClickListener {
                startActivity(Intent(context, SignupActivity::class.java))
            }
        }

        loginBtn = findViewById<Button>(R.id.login_button).apply {
            setOnClickListener {
                if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                    loginUser( email.text.toString(), password.text.toString())
                }
            }
        }
    }

    private fun loginUser(user: String, password: String){
        auth?.signInWithEmailAndPassword(user, password)
            ?.addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(LogUtil.TAG, "Signed in successfully")
                    prefs.updateCurrentUser(user, password)
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Log.d(LogUtil.TAG, "User does not exist")
                }
            }
    }
}

