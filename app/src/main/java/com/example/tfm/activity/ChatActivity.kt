package com.example.tfm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.tfm.R
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        setSupportActionBar(toolbar)
        displayBackArrow()

        sendButton.setOnClickListener {
            toast("Sending...${chat_edittext.text}")
        }

    }

    private fun displayBackArrow(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
