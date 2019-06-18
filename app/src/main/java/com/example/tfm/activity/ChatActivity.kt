package com.example.tfm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.bundled.BundledEmojiCompatConfig
import android.support.text.emoji.widget.EmojiEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast

class ChatActivity : AppCompatActivity() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var emojiEditText: EmojiEditText

    companion object{
        var messages = mutableListOf<Message>()
        lateinit var messagesRecyclerView: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEmoji()
        setContentView(R.layout.activity_chat)
        setSupportActionBar(chat_toolbar)
        displayBackArrow()

        emojiEditText = findViewById(R.id.chat_edittext)


        initListeners()

        //sample messages
        messages.add(Message(Sender.OWN, MessageType.MESSAGE, "Hello World",  1212, "EN" ))
        messages.add(Message(Sender.OTHER, MessageType.MESSAGE, getString(R.string.dwight_quote), 1213 , "EN"))

        viewManager = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(messages)

        messagesRecyclerView = findViewById<RecyclerView>(R.id.chat_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if(bottom < oldBottom){
                    postDelayed({ scrollToPosition(viewAdapter.itemCount - 1) }, 0)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {

        R.id.call -> {
            toast("Phonecalling...")
            true
        }

        R.id.videocall -> {
            toast("Videocalling...")
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun displayBackArrow(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    private fun initListeners(){
        emojiButton.setOnClickListener {
            toast("Emoji")
        }
        pictureButton.setOnClickListener {
            toast("Photo")
        }
        gifButton.setOnClickListener {
            toast("GIF")
        }
        cameraButton.setOnClickListener {
            toast("Camera")
        }
        micButton.setOnClickListener {
            toast("Microphone")
        }
        locationButton.setOnClickListener {
            toast("Location")
        }
        attachmentButton.setOnClickListener {
            toast("Attachment")
        }
        codeButton.setOnClickListener {
            toast("Code")
        }

        sendButton.setOnClickListener {
            val fieldText = chat_edittext.text.toString()
            if(fieldText.isNotEmpty()){
                messages.add(Message(Sender.OWN, MessageType.MESSAGE, fieldText, 1, "EN" ))
                messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
                viewAdapter.notifyDataSetChanged()
                chat_edittext.text.clear()
            }
        }
    }
}
