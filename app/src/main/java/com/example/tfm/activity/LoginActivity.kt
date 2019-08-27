package com.example.tfm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tfm.R
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)

        signupBtn = findViewById<Button>(R.id.login_signup_button).apply {
            setOnClickListener {
                startActivity(Intent(context, SignupActivity::class.java))
            }
        }

        loginBtn = findViewById<Button>(R.id.login_button).apply {
            toast("Login in...")
        }
    }
}
