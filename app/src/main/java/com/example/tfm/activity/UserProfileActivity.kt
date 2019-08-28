package com.example.tfm.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R

class UserProfileActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toolbar = findViewById(R.id.profile_toolbar)
        toolbar.title = ""

        displayArrowBack(toolbar)
    }

    private fun displayArrowBack(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
