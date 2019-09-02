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
import com.example.tfm.util.trimBothSides
import com.example.tfm.util.updateCurrentUser
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

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

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        signupBtn = findViewById(R.id.login_signup_button)
        loginBtn = findViewById(R.id.login_button)

        auth = FirebaseAuth.getInstance()

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val (emailPref, passwordPref) = prefs.getCredentials()

        if(emailPref.isNotEmpty() && passwordPref.isNotEmpty()){
            loginUser(emailPref.trimBothSides(), passwordPref)
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        loginBtn.setOnClickListener {
            if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                loginUser( email.text.toString().trimBothSides(), password.text.toString())
            }
        }
    }

    private fun loginUser(user: String, password: String){
        disableViews()
        auth?.signInWithEmailAndPassword(user, password)
            ?.addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(LogUtil.TAG, "Signed in successfully")
                    prefs.updateCurrentUser(user, password)
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    toast("Wrong user/password")
                    enableViews()
                }
            }
    }

    private fun disableViews(){
        this.email.isEnabled = false
        this.password.isEnabled = false
        this.signupBtn.isEnabled = false
        this.loginBtn.isEnabled = false
    }

    private fun enableViews(){
        this.email.isEnabled = true
        this.password.isEnabled = true
        this.signupBtn.isEnabled = true
        this.loginBtn.isEnabled = true
    }
}

