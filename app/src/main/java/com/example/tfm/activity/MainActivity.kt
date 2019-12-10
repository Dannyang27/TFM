package com.example.tfm.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.tfm.R
import com.example.tfm.adapter.ConversationAdapter
import com.example.tfm.data.DataRepository
import com.example.tfm.data.DataRepository.isServiceRunning
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.model.Conversation
import com.example.tfm.notification.MyNotificationManager
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.service.FirebaseListenerService
import com.example.tfm.util.LogUtil
import com.example.tfm.util.clearCredential
import com.example.tfm.viewmodel.ConversationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @BindView(R.id.search_chat) lateinit var searcher: SearchView
    @BindView(R.id.fab) lateinit var fab: FloatingActionButton
    @BindView(R.id.conversations_recyclerview) lateinit var conversations: RecyclerView

    private lateinit var conversationViewModel: ConversationViewModel
    private lateinit var firebaseService: Intent
    private var conversationIds = mutableListOf<String>()

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter : ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setupInitialiser()

        toolbar_title.text = getString(R.string.messages)
        setSupportActionBar(my_toolbar)
        firebaseService = Intent(applicationContext, FirebaseListenerService::class.java)

        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)
        conversationViewModel.getConversations().observe(this, Observer { list ->
            viewAdapter.updateList(list)
            restartServiceIfChanged(list)
        })

        conversationViewModel.initRoomObserver(this)
        downloadUserDataIfNew()

        searcher.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    search_chat.clearFocus()
                }

                conversationViewModel.filterList(newText)
                return true
            }
        })

        initRecyclerView()
    }

    private fun setupInitialiser(){
        initEmoji()
        initDatabase()
        DataRepository.initTranslator(applicationContext)
        MyNotificationManager.createNotificationChannel(this)
    }

    private fun initRecyclerView(){
        viewManager = LinearLayoutManager(this)
        viewAdapter = ConversationAdapter(mutableListOf())

        conversations.apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun restartServiceIfChanged(list: MutableList<Conversation>){
        var hasListChanged = false

        list.forEach {
            if(it.id !in conversationIds){
                conversationIds.add(it.id)
                hasListChanged = true
            }
        }

        if(hasListChanged){
            stopService(firebaseService)
            startService(firebaseService)
            Log.d(LogUtil.TAG, "Restarting service...")
        }
    }

    private fun downloadUserDataIfNew() {
        val fromLoginActivity = intent.getBooleanExtra("fromLogin", false)
        if (fromLoginActivity) {
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
            createSignoutDialog().show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun createSignoutDialog(): AlertDialog{
        return AlertDialog.Builder(this)
            .setTitle(R.string.signout)
            .setMessage(R.string.signout_alert)
            .setPositiveButton(R.string.sure) { _, _ ->
                signout()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

            .create()
    }

    private fun signout(){
        FirebaseAuth.getInstance().signOut()
        PreferenceManager.getDefaultSharedPreferences(applicationContext).clearCredential()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {}

    private fun initEmoji() = EmojiCompat.init(BundledEmojiCompatConfig(applicationContext))

    private fun initDatabase() = MyRoomDatabase.getMyRoomDatabase(this)

    override fun onResume() {
        super.onResume()
        if(!isServiceRunning(this)) {
            startService(firebaseService)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(firebaseService)
    }

    @OnClick(R.id.fab)
    fun fabClick(){
        startActivity(Intent(this, UserSearcherActivity::class.java))
    }
}