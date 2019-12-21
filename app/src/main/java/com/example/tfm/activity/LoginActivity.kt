package com.example.tfm.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.tfm.R
import com.example.tfm.util.*
import com.example.tfm.viewmodel.LoginViewModel
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.login_email) lateinit var eEmail: EditText
    @BindView(R.id.login_password) lateinit var ePassword: EditText
    @BindView(R.id.login_signup_button) lateinit var bSignup: Button
    @BindView(R.id.login_button) lateinit var bLogin: Button
    @BindView(R.id.login_progressbar) lateinit var progressBar: ProgressBar

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
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

        val (emailPref, passwordPref) = PreferenceManager.getDefaultSharedPreferences(this).getCredentials()

        if( isFormNotEmpty(emailPref, passwordPref) ){
            login(emailPref.trimBothSides(), passwordPref)
        }
    }

    @OnClick(R.id.login_signup_button)
    fun launchSignup(){
        startActivity(Intent(this, SignupActivity::class.java))
    }

    @OnClick(R.id.login_button)
    fun login(){
        if(eEmail.text.isNotEmpty() && ePassword.text.isNotEmpty()){
            login(eEmail.text.toString().trimBothSides(), ePassword.text.toString())
        }else{
            toast(R.string.field_not_empty)
        }
    }

    private fun login(user: String, password: String){
        loginViewModel.startLogin(this, user, password)
    }

    private fun isFormNotEmpty(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun disableViews(){
        progressBar.start()
        eEmail.isEnabled = false
        ePassword.isEnabled = false
        bSignup.isEnabled = false
        bLogin.isEnabled = false
    }

    private fun enableViews(){
        progressBar.stop()
        eEmail.isEnabled = true
        ePassword.isEnabled = true
        bSignup.isEnabled = true
        bLogin.isEnabled = true
    }
}