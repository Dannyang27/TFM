package com.example.tfm.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import org.jetbrains.anko.toast

class UserProfileActivity : AppCompatActivity() {

    private lateinit var username: LinearLayout
    private lateinit var status: LinearLayout
    private lateinit var email: LinearLayout

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toolbar = findViewById(R.id.profile_toolbar)
        toolbar.title = getString(R.string.profile)

        username = findViewById(R.id.profile_username_layout)
        status = findViewById(R.id.profile_status_layout)
        email = findViewById(R.id.profile_email_layout)

        username.setOnClickListener {
            toast("Changing username")
        }

        status.setOnClickListener {
            toast("Changing status")
        }

        email.setOnClickListener {
            toast("Changing email")
        }

        displayArrowBack(toolbar)
    }

    private fun displayArrowBack(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
