package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.*
import org.jetbrains.anko.toast

class ImageToolActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var image: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var rotateBtn: ImageButton
    private lateinit var acceptBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var bitmap: Bitmap

    private lateinit var content: String
    private var isProfilePhoto = false

    companion object {
        var source = MediaSource.NONE

        fun launchImageTool(context: Context, uri: Uri?, source: MediaSource, isProfilePhoto: Boolean) {
            this.source = source

            val intent = Intent(context, ImageToolActivity::class.java)
            intent.putExtra("imageUrl", uri?.toString())
            intent.putExtra("isProfilePhoto", isProfilePhoto)

            if(source == MediaSource.GIF){
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_tool)

        val uri = intent.getStringExtra("imageUrl")
        isProfilePhoto = intent.getBooleanExtra("isProfilePhoto", false)

        toolbar = findViewById(R.id.tool_toolbar)
        image = findViewById(R.id.tool_image)
        cancelBtn = findViewById(R.id.tool_cancel)
        rotateBtn = findViewById(R.id.tool_rotate)
        acceptBtn = findViewById(R.id.tool_accept)
        progressBar = findViewById(R.id.tool_progressbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var typeValue = MessageType.IMAGE.value

        when(source){
            MediaSource.GALLERY -> {
                bitmap = loadImageFromUri(uri)
                setBitmapToImageView(image, bitmap)
                content = bitmap.toBase64()
            }

            MediaSource.CAMERA -> {
                bitmap = BitmapFactory.decodeFile(uri)
                setBitmapToImageView(image, bitmap)
                content = bitmap.toBase64()
            }

            MediaSource.GIF -> {
                rotateBtn.visibility = View.GONE
                loadGif(uri)
                typeValue = MessageType.GIF.value
                content = uri
            }

            else -> {}
        }

        initAcceptButton(typeValue, content)

        cancelBtn.setOnClickListener {
            finish()
        }

        rotateBtn.setOnClickListener {
            image.rotate()
        }
    }

    private fun loadGif(gifUrl: String?) {
        progressBar.start()
        gifUrl?.let {
            Glide.with(image)
                .asGif()
                .load(it)
                .override(image.width, image.width / 2)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                    override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                        progressBar.stop()
                        return false                    }
                })
                .into(image)
        }
    }

    private fun initAcceptButton(typeValue: Int, content: String){
        acceptBtn.setOnClickListener {

            if( isProfilePhoto ){
                toast("Setting profile photo")
            }else{
                val timestamp = System.currentTimeMillis()
                val message = Message(timestamp, ChatActivity.conversationId, DataRepository.currentUserEmail,
                    ChatActivity.receiverUser, typeValue, MessageContent(fieldOne = content), timestamp)

                FirebaseUtil.addMessageLocal(message)
                FirebaseUtil.addMessageFirebase(this, message)
            }

            finish()
        }
    }
}
