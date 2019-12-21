package com.example.tfm.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.util.launchMainActivity
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.example.tfm.util.trimBothSides
import com.example.tfm.viewmodel.SignupViewModel
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    @BindView(R.id.signup_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.signup_user) lateinit var eUser: EditText
    @BindView(R.id.signup_email) lateinit var eEmail: EditText
    @BindView(R.id.signup_password) lateinit var ePassword: EditText
    @BindView(R.id.signup_joinus) lateinit var bJoinus: Button
    @BindView(R.id.signup_progressbar) lateinit var progressBar: ProgressBar

    companion object{
        lateinit var currentUserEmail: String
        lateinit var currentUserPassword: String
    }

    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        signupViewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
        signupViewModel.getIsJoinSuccessful().observe(this, Observer {isSuccess ->
            if(isSuccess){
                DataRepository.currentUserEmail = eEmail.text.toString().trimBothSides()
                launchMainActivity()
            }else{
                enableViews()
            }
        })

        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun disableViews(){
        progressBar.start()
        eUser.isEnabled = false
        eEmail.isEnabled = false
        ePassword.isEnabled = false
        bJoinus.isEnabled = false
    }
    private fun enableViews(){
        progressBar.stop()
        eUser.isEnabled = true
        eEmail.isEnabled = true
        ePassword.isEnabled = true
        bJoinus.isEnabled = true
    }

    private fun isFormNotEmpty() = eUser.text.isNotEmpty() && eEmail.text.isNotEmpty() && ePassword.text.isNotEmpty()

    @OnClick(R.id.signup_joinus)
    fun signup(){
        if(isFormNotEmpty()){
            disableViews()
            val username = eUser.text.toString().trimBothSides()
            val email = eEmail.text.toString().trimBothSides()
            val password = ePassword.text.toString().trimBothSides()
            signupViewModel.joinNewUser(this, username, email, password)
        }else{
            toast(R.string.field_not_empty)
        }
    }
}