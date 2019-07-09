package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.emoji.widget.EmojiEditText
import androidx.emoji.widget.EmojiTextView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import org.jetbrains.anko.toast

class ImageSenderActivity : AppCompatActivity() {

    companion object{
        fun launchActivity(context: Context, gifUrl: String){
            val intent = Intent(context, ImageSenderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("gifUrl", gifUrl)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_sender)
        val toolbar = findViewById<Toolbar>(R.id.image_sender_toolbar)
        val media = findViewById<ImageView>(R.id.image_sender_media)
        val sendBtn = findViewById<ImageButton>(R.id.image_sender_button)
        val captionEt = findViewById<EmojiEditText>(R.id.image_sender_caption)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        val receiver = findViewById<EmojiTextView>(R.id.image_sender_user)
        receiver.text = getString(R.string.username_sample)

        val url = intent.getStringExtra("gifUrl")
        url?.let {
            Glide.with(media.context)
                .asGif()
                .centerCrop()
                .load(url)
                .override(media.width/4,media.height/4)
                .into(media)
        }.also {
            Log.d("DEBUG", url)
        }

        sendBtn.setOnClickListener {
            toast("Sending Gif...")
            ChatActivity.sendMessage(Message(Sender.OWN, MessageType.GIF, MediaContent(url, captionEt.text.toString()), 10, "EN" ))
            onBackPressed()
        }
    }
}
