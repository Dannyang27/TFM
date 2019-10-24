package com.example.tfm.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.util.launchMainActivity
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.example.tfm.util.trimBothSides
import com.example.tfm.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    companion object{
        lateinit var currentUserEmail: String
        lateinit var currentUserPassword: String
    }

    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupViewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)

        signupViewModel.getIsJoinSuccessful().observe(this, Observer {isSuccess ->
            if(isSuccess){
                DataRepository.currentUserEmail = signup_email.text.toString().trimBothSides()
                launchMainActivity(false)
            }else{
                enableViews()
            }
        })

        signup_toolbar.title = ""
        setSupportActionBar(signup_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        signup_joinus.setOnClickListener {
            if(isFormNotEmpty()){
                disableViews()
                val username = signup_user.text.toString().trimBothSides()
                val email = signup_email.text.toString().trimBothSides()
                val password = signup_password.text.toString().trimBothSides()
                signupViewModel.joinNewUser(this, username, email, password)
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

    private fun disableViews(){
        signup_progressbar.start()
        signup_user.isEnabled = false
        signup_email.isEnabled = false
        signup_password.isEnabled = false
        signup_joinus.isEnabled = false
    }
    private fun enableViews(){
        signup_progressbar.stop()
        signup_user.isEnabled = true
        signup_email.isEnabled = true
        signup_password.isEnabled = true
        signup_joinus.isEnabled = true
    }

    private fun isFormNotEmpty(): Boolean{
        return signup_user.text.isNotEmpty()
                && signup_email.text.isNotEmpty()
                && signup_password.text.isNotEmpty()
    }
}