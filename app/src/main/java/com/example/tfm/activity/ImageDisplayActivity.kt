package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.tfm.enum.MediaSource
import com.example.tfm.util.LogUtil
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_image_display.*

class ImageDisplayActivity : AppCompatActivity() {

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

        imagedisplay_layout.setOnClickListener {
            if(isToolbarVisible){
                isToolbarVisible = false
                imagedisplay_toolbar.visibility = View.INVISIBLE
                imagedisplay_labels.visibility = View.INVISIBLE
            }else{
                isToolbarVisible = true
                imagedisplay_toolbar.visibility = View.VISIBLE
                imagedisplay_labels.visibility = View.VISIBLE
            }
        }

        setSupportActionBar(imagedisplay_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if(source == MediaSource.GALLERY){
            loadBitmap(bitmap)
        }else{
            loadGif(uri)
        }
    }

    private fun loadBitmap(bitmap: Bitmap?){
        imagedisplay_progressbar.start()

        Glide.with(this)
            .load(bitmap)
            .into(imagedisplay_image)

        val image = FirebaseVisionImage.fromBitmap(bitmap!!)
        val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

        labeler.processImage(image)
            .addOnSuccessListener {
                it.forEachIndexed { index, label ->
                    if(index == 1){
                        labelOne.text = "${label.text}:"
                        confidenceOne.text = label.confidence.toString()
                    }else if(index == 2){
                        labelTwo.text = "${label.text}:"
                        confidenceTwo.text = label.confidence.toString()
                    }else if(index == 3){
                        labelThree.text = "${label.text}:"
                        confidenceThree.text = label.confidence.toString()
                    }
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "failure")
            }

        imagedisplay_progressbar.stop()
    }

    private fun loadGif(uri: Uri?){
        imagedisplay_progressbar.start()

        Glide.with(this)
            .asGif()
            .load(uri)
            .override(imagedisplay_image.width, imagedisplay_image.width / 2)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?, source: DataSource?, isFirstRes: Boolean): Boolean {
                    imagedisplay_progressbar.stop()
                    return false                    }
            })
            .into(imagedisplay_image)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
