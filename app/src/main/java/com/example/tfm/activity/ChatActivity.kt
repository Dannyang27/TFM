package com.example.tfm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.EditText
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        var messages = mutableListOf<Message>()
        lateinit var messagesRecyclerView: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        setSupportActionBar(toolbar)
        displayBackArrow()

        val messageField = findViewById<EditText>(R.id.chat_edittext)

        sendButton.setOnClickListener {
            val fieldText = messageField.text.toString()
            if(fieldText.isNotEmpty()){
                messages.add(Message(Sender.OWN, MessageType.MESSAGE, fieldText, 1, "EN" ))
                messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
                viewAdapter.notifyDataSetChanged()
                messageField.text.clear()
            }
        }

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

    private fun displayBackArrow(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
