package com.example.tfm.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.fragments.GroupChatFragment
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.LogUtil
import com.example.tfm.util.clearCredential
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

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
                val intent = Intent(this, UserSearcherActivity::class.java)
                startActivity(intent)
            }else{
                //TODO add search group
                launch {
                    async{MyRoomDatabase.getMyRoomDatabase(toolbar.context)?.deleteAllConversation()}.await()
                    Log.d(LogUtil.TAG, "Room database conversation removed")
                }
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {

        R.id.profile -> {
            startActivity(Intent(this, UserProfileActivity::class.java))
            true
        }

        R.id.settings -> {
            toast("Settings Clicked")
            true
        }

        R.id.signout -> {
            FirebaseAuth.getInstance().signOut()
            PreferenceManager.getDefaultSharedPreferences(this).clearCredential()
            startActivity(Intent(this, LoginActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {}

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}
