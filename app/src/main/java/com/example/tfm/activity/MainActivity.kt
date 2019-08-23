package com.example.tfm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.emoji.text.EmojiCompat
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import android.widget.SearchView
import android.widget.TextView
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.fragments.GroupChatFragment
import org.jetbrains.anko.toast
import android.view.*
import com.example.tfm.R
import com.example.tfm.util.getAllUsers
import com.example.tfm.util.loadFakeUsers
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val privateFragment = PrivateFragment.newInstance()
    private val groupChatFragment = GroupChatFragment.newInstance()
    private var activeFragment: Fragment = privateFragment
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var fab: FloatingActionButton

    private lateinit var database: DatabaseReference

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_chat -> {
                fab.setImageDrawable(getDrawable(R.drawable.add_user))
                openFragment(privateFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_groupchat -> {
                fab.setImageDrawable(getDrawable(R.drawable.add_group))
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
        initEmoji()
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.messages)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            if(activeFragment is PrivateFragment){
                toast("Creating private chat...")
                val intent = Intent(this, UserSearcherActivity::class.java)
                startActivity(intent)
            }else{
                toast("Creating group chat...")
            }
        }

        searchView = findViewById(R.id.search_chat)

        val navView: BottomNavigationView = findViewById(R.id.navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, groupChatFragment, "2")
            .hide(groupChatFragment)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, privateFragment, "1")
            .commit()


        //TEST FIREBASE
        database = FirebaseDatabase.getInstance().reference
        //database.loadFakeUsers()
        //database.getAllUsers()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {

        R.id.settings -> {
            toast("Settings Clicked")
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}
