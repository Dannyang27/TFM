package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.rotate
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.example.tfm.util.toBase64
import com.example.tfm.viewmodel.ImageToolViewModel
import kotlinx.android.synthetic.main.activity_image_tool.*

class ImageToolActivity : AppCompatActivity() {

    private lateinit var content: String
    private lateinit var imageToolViewModel: ImageToolViewModel

    companion object {
        var source = MediaSource.NONE

        fun launchImageTool(context: Context, uri: Uri?, source: MediaSource) {
            this.source = source

            val intent = Intent(context, ImageToolActivity::class.java)
            intent.putExtra("imageUrl", uri?.toString())

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

        setSupportActionBar(tool_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var typeValue = MessageType.IMAGE.value

        if(source != MediaSource.GIF){
            imageToolViewModel = ViewModelProviders.of(this).get(ImageToolViewModel::class.java)

            imageToolViewModel.getImage().observe(this, Observer {bitmap ->
                tool_touchimage.setImageBitmap(bitmap)
                tool_image.visibility = View.GONE
                tool_touchimage.visibility = View.VISIBLE
                content = bitmap.toBase64()
            })

            imageToolViewModel.initImageBySource(this, uri, source)

        }else {
            tool_rotate.visibility = View.GONE
            loadGif(uri)
            typeValue = MessageType.GIF.value
            content = uri
        }

        initAcceptButton(typeValue)
        initButtonListeners()
    }

    private fun loadGif(gifUrl: String?) {
        tool_progressbar.start()
        gifUrl?.let {
            Glide.with(this)
                .asGif()
                .load(it)
                .override(tool_image.width, tool_image.width / 2)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                    override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                        tool_progressbar.stop()
                        tool_image.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(tool_image)
        }
    }

    private fun initAcceptButton(typeValue: Int){
        tool_accept.setOnClickListener {
            val timestamp = System.currentTimeMillis()
            val message = Message(timestamp, ChatActivity.conversationId, DataRepository.currentUserEmail,
                ChatActivity.receiverUser, typeValue, MessageContent(fieldOne = content), timestamp)

            val roomDatabase = MyRoomDatabase.getMyRoomDatabase(this)
            roomDatabase?.addMessage(message)
            finish()
        }
    }

    private fun initButtonListeners(){
        tool_cancel.setOnClickListener {
            finish()
        }

        tool_rotate.setOnClickListener {
            tool_touchimage.rotate()
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