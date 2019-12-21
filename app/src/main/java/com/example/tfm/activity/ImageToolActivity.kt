package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tfm.R
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.*
import com.example.tfm.viewmodel.ImageToolViewModel
import com.ortiz.touchview.TouchImageView

class ImageToolActivity : AppCompatActivity() {

    @BindView(R.id.tool_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.tool_touchimage) lateinit var touchImage: TouchImageView
    @BindView(R.id.tool_image) lateinit var image: ImageView
    @BindView(R.id.tool_progressbar) lateinit var progressBar: ProgressBar
    @BindView(R.id.tool_cancel) lateinit var bCancel: Button
    @BindView(R.id.tool_rotate) lateinit var bRotate: ImageButton
    @BindView(R.id.tool_accept) lateinit var bAccept: Button

    private lateinit var content: String
    private lateinit var imageToolViewModel: ImageToolViewModel
    private var typeValue: Int = MessageType.IMAGE.value

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
        ButterKnife.bind(this)

        val uri = intent.getStringExtra("imageUrl")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if(source != MediaSource.GIF){
            imageToolViewModel = ViewModelProviders.of(this).get(ImageToolViewModel::class.java)

            imageToolViewModel.getImage().observe(this, Observer {bitmap ->
                touchImage.setImageBitmap(bitmap)
                touchImage.visibility = View.VISIBLE
                image.visibility = View.GONE
                content = bitmap.toBase64()
            })

            imageToolViewModel.initImageBySource(this, uri, source)

        }else { loadGif(uri) }
    }

    private fun loadGif(gifUrl: String?) {
        bRotate.visibility = View.GONE
        typeValue = MessageType.GIF.value
        content = gifUrl.toString()
        progressBar.start()

        gifUrl?.let {
            Glide.with(this)
                .asGif()
                .load(it)
                .override(image.width, image.width / 2)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                    override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                        progressBar.stop()
                        image.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(image)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.tool_accept)
    fun accept(){
        val timestamp = System.currentTimeMillis()
        val message = Message(timestamp, ChatActivity.conversationId, currentUserEmail,
            ChatActivity.receiverUser, typeValue, MessageContent(fieldOne = content), timestamp)

        FirebaseUtil.addMessageFirebase(message)
        finish()
    }

    @OnClick(R.id.tool_cancel)
    fun cancel(){
        finish()
    }

    @OnClick(R.id.tool_rotate)
    fun rotate(){
        content = touchImage.rotate()
    }
}