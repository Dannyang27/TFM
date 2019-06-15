package com.example.tfm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.tfm.R
import org.jetbrains.anko.toast


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        setSupportActionBar(toolbar)
        displayBackArrow()
    }

    private fun displayBackArrow(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.chat_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
//
//        R.id.videocall -> {
//            toast("Videocalling...")
//            true
//        }
//
//        R.id.call -> {
//            toast("Phonecalling...")
//            true
//        }
//
//        else -> super.onOptionsItemSelected(item)
//    }
}
