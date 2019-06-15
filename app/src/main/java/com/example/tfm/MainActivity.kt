package com.example.tfm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.example.tfm.fragments.ChatFragment
import com.example.tfm.fragments.GroupChatFragment
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val chatFragment = ChatFragment.newInstance()
    private val groupChatFragment = GroupChatFragment.newInstance()
    private var activeFragment: Fragment = chatFragment

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_chat -> {
                openFragment(chatFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_groupchat -> {
                openFragment(groupChatFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().add(R.id.container, groupChatFragment, "2").hide(groupChatFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container, chatFragment, "1").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        R.id.search -> {
            toast("Searchs Clicked")
            true
        }

        R.id.new_group -> {
            toast("New groups Clicked")
            true
        }

        R.id.settings -> {
            toast("Settings Clicked")
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
