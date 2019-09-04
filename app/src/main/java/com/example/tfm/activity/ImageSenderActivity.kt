package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.emoji.widget.EmojiEditText
import androidx.emoji.widget.EmojiTextView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.LogUtil
import com.example.tfm.util.toBase64
import org.jetbrains.anko.toast

class ImageSenderActivity : AppCompatActivity() {

    private lateinit var media: ImageView
    private lateinit var sendBtn: ImageButton
    private lateinit var captionEt: EmojiEditText

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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val receiver = findViewById<EmojiTextView>(R.id.image_sender_user)
        receiver.text = getString(R.string.username_sample)

        when(source){
            MediaSource.GALLERY -> loadImageFromGallery()
            MediaSource.CAMERA -> loadImageFromCamera()
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
                .into(media)

            sendBtn.setOnClickListener {
                val timestamp = System.currentTimeMillis()
                val message = Message(
                    id = timestamp,
                    messageType = MessageType.GIF.value,
                    body = MessageContent(fieldOne = url, fieldTwo = captionEt.text.toString()),
                    timestamp = timestamp,
                    languageCode = "EN" )
                FirebaseUtil.addMessage(message)
                finish()
            }
        }
    }

    private fun loadImageFromGallery(){
        val filePath : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val uriString = intent.getStringExtra("imageUrl")

        uriString?.let{
            val uri = Uri.parse(it)
            val cursor = contentResolver.query(uri, filePath, null, null, null)
            if(cursor!!.moveToFirst()){
                val columnIndex = cursor.getColumnIndex(filePath[0])
                val fileP = cursor.getString(columnIndex)
                loadBitmap(BitmapFactory.decodeFile(fileP))
            }
            cursor.close()
        }
    }

    private fun loadImageFromCamera(){
        val uriString = intent.getStringExtra("imageUrl")
        uriString?.let{
            loadBitmap(BitmapFactory.decodeFile(it))
        }
    }

    private fun loadBitmap(bitmap: Bitmap){
        val imageAspectRatio = bitmap.height / bitmap.width

        Glide.with(this)
            .load(bitmap)
            .override(media.width, media.width * imageAspectRatio)
            .into(media)

        sendBtn.setOnClickListener {
            Log.d(LogUtil.TAG, "Sending image...")
            val timestamp = System.currentTimeMillis()
            val message = Message(
                id = timestamp,
                messageType = MessageType.IMAGE.value,
                body = MessageContent(fieldOne = bitmap.toBase64(), fieldTwo = captionEt.text.toString()),
                timestamp = timestamp,
                languageCode = "EN")
            FirebaseUtil.addMessage(message)
            finish()
        }
    }
}