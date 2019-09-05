package com.example.tfm.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.util.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Default + job

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var prefs: SharedPreferences
    private var auth: FirebaseAuth? = null

    companion object{
        lateinit var progressBar: ProgressBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        signupBtn = findViewById(R.id.login_signup_button)
        loginBtn = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.login_progressbar)

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
        progressBar.start()
        disableViews()
        auth?.signInWithEmailAndPassword(user, password)
            ?.addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(LogUtil.TAG, "Signed in successfully")
                    prefs.updateCurrentUser(user, password)

                    launch {
                        FirebaseUtil.loadUserConversation(this@LoginActivity, user)
                    }

                }else{
                    toast("Wrong user/password")
                    enableViews()
                    progressBar.stop()
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

