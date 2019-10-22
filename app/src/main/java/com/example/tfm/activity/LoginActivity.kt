package com.example.tfm.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tfm.R
import com.example.tfm.util.*
import com.example.tfm.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        loginViewModel.getIsLoading().observe(this, Observer { isLoading ->
            if(isLoading){
                disableViews()
            }else{
                enableViews()
            }
        })

        loginViewModel.getIsSuccessful().observe(this, Observer { authIsSuccess ->
            if(authIsSuccess){
                loginViewModel.reset()
                launchMainActivity()
            }
        })

        val (emailPref, passwordPref) = prefs.getCredentials()

        if( isFormNotEmpty(emailPref, passwordPref) ){
            login(emailPref.trimBothSides(), passwordPref)
        }

        initListeners()
    }

    private fun initListeners(){
        login_signup_button.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        login_button.setOnClickListener {
            if(login_email.text.isNotEmpty() && login_password.text.isNotEmpty()){
                login(login_email.text.toString().trimBothSides(), login_password.text.toString())
            }
        }
    }

    private fun login(user: String, password: String){
        loginViewModel.startLogin(this, user, password)
    }

    private fun isFormNotEmpty(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun disableViews(){
        login_progressbar.start()
        login_email.isEnabled = false
        login_password.isEnabled = false
        login_signup_button.isEnabled = false
        login_button.isEnabled = false
    }

    private fun enableViews(){
        login_progressbar.stop()
        login_email.isEnabled = true
        login_password.isEnabled = true
        login_signup_button.isEnabled = true
        login_button.isEnabled = true
    }
}

