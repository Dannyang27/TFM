package com.example.tfm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var user : EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var joinusBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        toolbar = findViewById(R.id.signup_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = findViewById(R.id.signup_user)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        joinusBtn = findViewById<Button>(R.id.signup_joinus).apply {
            this.setOnClickListener {
                toast("Creating User...")
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
}
