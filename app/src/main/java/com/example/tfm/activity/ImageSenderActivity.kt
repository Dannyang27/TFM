package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.emoji.widget.EmojiEditText
import androidx.emoji.widget.EmojiTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tfm.R
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.*
import org.jetbrains.anko.toast

class ImageSenderActivity : AppCompatActivity() {

    private lateinit var media: ImageView
    private lateinit var sendBtn: ImageButton
    private lateinit var captionEt: EmojiEditText
    private lateinit var bitmap: Bitmap
    private lateinit var progressbar: ProgressBar

    companion object{
        var source = MediaSource.NONE

        fun launchActivityWithGif(context: Context, gifUrl: String, source: MediaSource){
            val intent = Intent(context, ImageSenderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("gifUrl", gifUrl)
            this.source = source
            context.startActivity(intent)
        }

        fun launchActivityWithImage(context: Context, uri: Uri?, source: MediaSource){
            val intent = Intent(context, ImageSenderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("imageUrl", uri?.toString())
            this.source = source
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_sender)
        val toolbar = findViewById<Toolbar>(R.id.image_sender_toolbar)
        media = findViewById(R.id.image_sender_media)
        sendBtn = findViewById(R.id.image_sender_button)
        captionEt = findViewById(R.id.image_sender_caption)
        progressbar = findViewById(R.id.media_progressbar)

        progressbar.start()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val receiver = findViewById<EmojiTextView>(R.id.image_sender_user)
        receiver.text = getString(R.string.username_sample)

        when(source) {
            MediaSource.GALLERY -> {
                val uri = intent.getStringExtra("imageUrl")
                bitmap = loadImageFromUri(uri)
                progressbar.stop()
                setBitmapToImageView(media, bitmap)
                initOnClickListerner()
            }

            MediaSource.CAMERA -> {
                loadImageFromCamera()
                initOnClickListerner()
            }

            MediaSource.GIF -> loadGif()
            else -> toast("No media source is found")
        }
    }

    private fun loadGif(){
        val url = intent.getStringExtra("gifUrl")
        url?.let {
            Glide.with(media.context)
                .asGif()
                .load(url)
                .override(media.width, media.width / 2)
                .listener(object: RequestListener<GifDrawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?,
                        isFirstResource: Boolean) = false

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?,
                        dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressbar.stop()
                        return false
                    }
                })
                .into(media)

            sendBtn.setOnClickListener {
                val timestamp = System.currentTimeMillis()
                val message = Message(
                    id = timestamp,
                    ownerId = ChatActivity.conversationId,
                    senderName = MainActivity.currentUserEmail,
                    receiverName = ChatActivity.receiverUser,
                    messageType = MessageType.GIF.value,
                    body = MessageContent(fieldOne = url, fieldTwo = captionEt.text.toString()),
                    timestamp = timestamp,
                    languageCode = "EN" )
                FirebaseUtil.addMessage(message)
                finish()
            }
        }
    }


    private fun loadImageFromCamera(){
        val uriString = intent.getStringExtra("imageUrl")
        uriString?.let{
            bitmap = BitmapFactory.decodeFile(it)
            setBitmapToImageView(media, bitmap)
        }
    }

    private fun initOnClickListerner(){
        sendBtn.setOnClickListener {
            Log.d(LogUtil.TAG, "Sending image...")
            val timestamp = System.currentTimeMillis()
            val message = Message(
                id = timestamp,
                ownerId = ChatActivity.conversationId,
                senderName = MainActivity.currentUserEmail,
                receiverName = ChatActivity.receiverUser,
                messageType = MessageType.IMAGE.value,
                body = MessageContent(fieldOne = bitmap.toBase64(), fieldTwo = captionEt.text.toString()),
                timestamp = timestamp,
                languageCode = "EN")
            FirebaseUtil.addMessage(message)
            finish()
        }
    }
}