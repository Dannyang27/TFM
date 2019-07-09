package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.emoji.widget.EmojiEditText
import androidx.emoji.widget.EmojiTextView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
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
            intent.putExtra("imageUrl", uri?.toString() )
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
                .centerCrop()
                .load(url)
                .override(media.width/4,media.height/4)
                .into(media)

            sendBtn.setOnClickListener {
                ChatActivity.sendMessage(Message(Sender.OWN, MessageType.GIF, MediaContent(url, captionEt.text.toString()), 10, "EN" ))
                onBackPressed()
            }
        }.also {
            Log.d("DEBUG", url)
        }
    }

    private fun loadImageFromGallery(){
        val filePath : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val uriString = intent.getStringExtra("imageUrl")

        uriString?.let{
            val uri = Uri.parse(it)

            val cursor = contentResolver.query(uri, filePath, null, null, null)
            if(cursor.moveToFirst()){
                val columnIndex = cursor.getColumnIndex(filePath[0])
                val fileP = cursor.getString(columnIndex)
                val imageBitmap = BitmapFactory.decodeFile(fileP)

                Glide.with(this)
                    .load(imageBitmap)
                    .centerCrop()
                    .into(media)

                sendBtn.setOnClickListener {
                    ChatActivity.sendMessage(Message(Sender.OWN, MessageType.PHOTO, MediaContent(imageBitmap, "captionTest"), 1 , "EN"))
                    onBackPressed()
                }
            }
            cursor.close()
        }
    }

    private fun loadImageFromCamera(){
        //TODO
    }
}