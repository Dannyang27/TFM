package com.example.tfm.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.fragments.GroupChatFragment
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.clearCredential
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val privateFragment = PrivateFragment.newInstance()
    private val groupChatFragment = GroupChatFragment.newInstance()
    private var activeFragment: Fragment = privateFragment

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

        toolbar_title.text = getString(R.string.messages)
        setSupportActionBar(my_toolbar)

        fab.setOnClickListener {
            if(activeFragment is PrivateFragment){
                val intent = Intent(this, UserSearcherActivity::class.java)
                startActivity(intent)
            }else{
                MyRoomDatabase.getMyRoomDatabase(this)?.deleteAllConversation()
            }
        }

        search_chat.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

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
            startActivity(Intent(this, MySettingActivity::class.java))
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