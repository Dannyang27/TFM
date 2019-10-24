package com.example.tfm.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.util.getCredentials

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val (email, password) = PreferenceManager.getDefaultSharedPreferences(this).getCredentials()
        var intent: Intent?

        if(email.isNotEmpty() && password.isNotEmpty()){
            intent = Intent(this, MainActivity::class.java)
        }else{
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
    }
}
