package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tfm.R
import com.example.tfm.enum.MediaSource
import com.example.tfm.util.LogUtil
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class ImageDisplayActivity : AppCompatActivity() {

    private lateinit var layout: RelativeLayout
    private lateinit var toolbar: Toolbar
    private lateinit var media: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var labelLayout: LinearLayout
    private lateinit var labelOne: TextView
    private lateinit var labelTwo: TextView
    private lateinit var labelThree: TextView
    private lateinit var confOne: TextView
    private lateinit var confTwo: TextView
    private lateinit var confThree: TextView


    private var isToolbarVisible = true

    companion object {
        var source = MediaSource.NONE
        var bitmap: Bitmap? = null
        var uri: Uri? = null

        fun launchBitmap(context: Context, bitmap: Bitmap?, source: MediaSource) {
            this.source = source
            this.bitmap = bitmap
            val intent = Intent(context, ImageDisplayActivity::class.java)
            context.startActivity(intent)
        }

        fun launchGif(context: Context, uri: Uri?, source: MediaSource){
            this.source = source
            this.uri = uri
            val intent = Intent(context, ImageDisplayActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        layout = findViewById(R.id.imagedisplay_layout)
        labelLayout = findViewById(R.id.imagedisplay_labels)
        labelOne = findViewById(R.id.labelOne)
        labelTwo = findViewById(R.id.labelTwo)
        labelThree = findViewById(R.id.labelThree)
        confOne = findViewById(R.id.confidenceOne)
        confTwo = findViewById(R.id.confidenceTwo)
        confThree = findViewById(R.id.confidenceThree)

        layout.setOnClickListener {
            if(isToolbarVisible){
                isToolbarVisible = false
                toolbar.visibility = View.INVISIBLE
                labelLayout.visibility = View.INVISIBLE
            }else{
                isToolbarVisible = true
                toolbar.visibility = View.VISIBLE
                labelLayout.visibility = View.VISIBLE
            }
        }

        toolbar = findViewById(R.id.imagedisplay_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressBar = findViewById(R.id.imagedisplay_progressbar)
        media = findViewById(R.id.imagedisplay_image)

        if(source == MediaSource.GALLERY){
            loadBitmap(bitmap)
        }else{
            loadGif(uri)
        }
    }

    private fun loadBitmap(bitmap: Bitmap?){
        progressBar.start()

        Glide.with(this)
            .load(bitmap)
            .into(media)

        val image = FirebaseVisionImage.fromBitmap(bitmap!!)
        val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

        labeler.processImage(image)
            .addOnSuccessListener {
                it.forEachIndexed { index, label ->
                    if(index == 1){
                        labelOne.text = "${label.text}:"
                        confOne.text = label.confidence.toString()
                    }else if(index == 2){
                        labelTwo.text = "${label.text}:"
                        confTwo.text = label.confidence.toString()
                    }else if(index == 3){
                        labelThree.text = "${label.text}:"
                        confThree.text = label.confidence.toString()
                    }
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "failure")
            }

        progressBar.stop()
    }

    private fun loadGif(uri: Uri?){
        progressBar.start()

        Glide.with(this)
            .asGif()
            .load(uri)
            .override(media.width, media.width / 2)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                    progressBar.stop()
                    return false                    }
            })
            .into(media)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
