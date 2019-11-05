package com.example.tfm.activity

import android.app.ActivityManager
import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.fragments.GroupChatFragment
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.service.FirebaseListenerService
import com.example.tfm.util.clearCredential
import com.example.tfm.viewmodel.ConversationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val privateFragment = PrivateFragment.newInstance()
    private val groupChatFragment = GroupChatFragment.newInstance()
    private var activeFragment: Fragment = privateFragment
    private lateinit var roomDatabase: MyRoomDatabase
    private lateinit var conversationViewModel: ConversationViewModel
    private lateinit var firebaseService: Intent

    private var conversationIds = mutableListOf<String>()

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
        firebaseService = Intent(this, FirebaseListenerService::class.java)

        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)
        conversationViewModel.getConversations().observe(this, Observer { list ->
            privateFragment.updateList(list)

            list.forEach {
                if(!conversationIds.contains(it.id)){
                    conversationIds.add(it.id)
                }
            }
        })

        conversationViewModel.initRoomObserver(this)
        conversationViewModel.updateUser(DataRepository.currentUserEmail)

        fab.setOnClickListener {
            if(activeFragment is PrivateFragment){
                val intent = Intent(this, UserSearcherActivity::class.java)
                startActivity(intent)
            }else{
                roomDatabase = MyRoomDatabase.getMyRoomDatabase(this)!!
                roomDatabase.getAllConversations(DataRepository.currentUserEmail)
            }
        }

        search_chat.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    search_chat.clearFocus()
                }

                conversationViewModel.filterList(newText)
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

        val fromLoginActivity = intent.getBooleanExtra("fromLogin", false)
        if(fromLoginActivity){
            conversationViewModel.initDownloadUserData()
        }else{
            DataRepository.initTranslator(this)
        }
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
            finish()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {}

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    override fun onResume() {
        super.onResume()
        if(!isServiceRunning()) {
            startService(firebaseService)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(firebaseService)
    }

    private fun isServiceRunning(): Boolean{
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE).forEach { serv ->
            if("com.example.tfm.service.FirebaseListenerService" == serv.service.className){
                return true
            }
        }

        return false
    }
}