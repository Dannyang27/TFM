package com.example.tfm.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
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
import com.example.tfm.util.FirebaseUtil
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
    private lateinit var dialog: Dialog

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

        FirebaseUtil.initRoomDatabase(this)

        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)
        conversationViewModel.getConversations().observe(this, Observer {
            privateFragment.updateList(it)
        })

        conversationViewModel.getDataDownloaded().observe(this, Observer {downloaded ->
            if(downloaded) {
                dialog.dismiss()
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
                roomDatabase.getAllMessages()
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

        val fromLoginActivity = intent.getBooleanExtra("fromLogin", false)
        if(fromLoginActivity){
            showDialog()
            conversationViewModel.initDownloadUserData()
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
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {}

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    private fun showDialog(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_progressbar)
        val dialogText = dialog.findViewById<TextView>(R.id.dialog_text)
        dialogText.text = getString(R.string.download_user_data)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}