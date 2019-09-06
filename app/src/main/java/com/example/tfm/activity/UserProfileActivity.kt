package com.example.tfm.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.toast

class UserProfileActivity : AppCompatActivity() {

    private lateinit var usernameLayout: LinearLayout
    private lateinit var statusLayout: LinearLayout
    private lateinit var username: TextView
    private lateinit var status: TextView
    private lateinit var email: TextView
    private lateinit var fab: FloatingActionButton

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toolbar = findViewById(R.id.profile_toolbar)
        toolbar.title = getString(R.string.profile)

        usernameLayout = findViewById(R.id.profile_username_layout)
        statusLayout = findViewById(R.id.profile_status_layout)
        username = findViewById(R.id.profile_username)
        status = findViewById(R.id.profile_status)
        email = findViewById(R.id.profile_email)
        fab = findViewById(R.id.profile_fab)

        fab.setOnClickListener {
            toast("Changing profile photo...")
        }

        usernameLayout.setOnClickListener {
            toast("Changing username")
        }

        statusLayout.setOnClickListener {
            toast("Changing status")
        }

        displayArrowBack(toolbar)
    }

    private fun displayArrowBack(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
