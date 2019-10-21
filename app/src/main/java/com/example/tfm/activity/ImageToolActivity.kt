package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_image_tool.*
import org.jetbrains.anko.toast

class ImageToolActivity : AppCompatActivity() {

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

        setSupportActionBar(tool_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var typeValue = MessageType.IMAGE.value

        when(source){
            MediaSource.GALLERY -> {
                if(isProfilePhoto){
                    //TODO show placeholder to match
                }
                bitmap = loadImageFromUri(uri)
                tool_image.setImageBitmap(bitmap)
                content = bitmap.toBase64()
            }

            MediaSource.CAMERA -> {
                bitmap = BitmapFactory.decodeFile(uri)
                tool_image.setImageBitmap(bitmap)
                content = bitmap.toBase64()
            }

            MediaSource.GIF -> {
                tool_rotate.visibility = View.GONE
                loadGif(uri)
                typeValue = MessageType.GIF.value
                content = uri
            }

            else -> {}
        }

        initAcceptButton(typeValue, content)

        tool_cancel.setOnClickListener {
            finish()
        }

        tool_rotate.setOnClickListener {
            tool_image.rotate()
        }
    }

    private fun loadGif(gifUrl: String?) {
        tool_progressbar.start()
        gifUrl?.let {
            Glide.with(tool_image)
                .asGif()
                .load(it)
                .override(tool_image.width, tool_image.width / 2)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                    override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                        tool_progressbar.stop()
                        return false                    }
                })
                .into(tool_image)
        }
    }

    private fun initAcceptButton(typeValue: Int, content: String){
        tool_accept.setOnClickListener {

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

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
