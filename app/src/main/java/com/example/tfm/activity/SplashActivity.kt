package com.example.tfm.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.getCredentials

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseUtil.initRoomDatabase(this)
        loginIfUnregistered()
    }

    private fun loginIfUnregistered(){
        val (email, password) = PreferenceManager.getDefaultSharedPreferences(this).getCredentials()
        var intent = if(userNotEmpty(email, password)) {
            Intent(this, MainActivity::class.java)
        }else { Intent(this, LoginActivity::class.java) }

        startActivity(intent)
    }

    private fun userNotEmpty(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()
}
