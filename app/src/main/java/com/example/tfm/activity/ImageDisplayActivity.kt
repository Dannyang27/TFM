package com.example.tfm.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.example.tfm.enum.MediaSource
import com.example.tfm.util.LogUtil
import com.example.tfm.util.start
import com.example.tfm.util.stop
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.activity_image_display.*
import org.jetbrains.anko.toast

class ImageDisplayActivity : AppCompatActivity() {

    @BindView(R.id.imagedisplay_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.imagedisplay_image) lateinit var image: ImageView
    @BindView(R.id.imagedisplay_touchimage) lateinit var touchImage: TouchImageView
    @BindView(R.id.imagedisplay_progressbar) lateinit var progressBar: ProgressBar
    @BindView(R.id.imagedisplay_labels) lateinit var layout: RelativeLayout
    @BindView(R.id.labelOne) lateinit var labelOne: TextView
    @BindView(R.id.confidenceOne) lateinit var confOne: TextView
    @BindView(R.id.labelTwo) lateinit var labelTwo: TextView
    @BindView(R.id.confidenceTwo) lateinit var confTwo: TextView
    @BindView(R.id.labelThree) lateinit var labelThree: TextView
    @BindView(R.id.confidenceThree) lateinit var confThree: TextView
    @BindView(R.id.imagedisplay_download) lateinit var downloadBtn: ImageButton

    private var isToolbarVisible = true

    companion object {
        var source = MediaSource.NONE
        var bitmap: Bitmap? = null
        var uri: Uri? = null

        fun launchBitmap(context: Context, bitmap: Bitmap?, source: MediaSource, view: View) {
            this.source = source
            this.bitmap = bitmap
            val intent = Intent(context, ImageDisplayActivity::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(
                context as Activity,  view, context.getString(R.string.media_transition))

            context.startActivity(intent, options.toBundle())
        }

        fun launchGif(context: Context, uri: Uri?, source: MediaSource, view: View) {
            this.source = source
            this.uri = uri
            val intent = Intent(context, ImageDisplayActivity::class.java)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (source == MediaSource.GALLERY) {
            touchImage.visibility = View.VISIBLE
            loadBitmap(bitmap)
            downloadBtn.visibility = View.VISIBLE
            image.visibility = View.GONE
        } else {
            image.visibility = View.VISIBLE
            loadGif(uri)
            downloadBtn.visibility = View.GONE
            touchImage.visibility = View.GONE
        }
    }

    private fun loadBitmap(bitmap: Bitmap?) {
        progressBar.start()

        touchImage.setImageBitmap(bitmap)

        val image = FirebaseVisionImage.fromBitmap(bitmap!!)
        val labeler = FirebaseVision.getInstance().onDeviceImageLabeler

        labeler.processImage(image)
            .addOnSuccessListener {
                it.forEachIndexed { index, label ->
                    when(index){
                        1 -> {  labelOne.text = "${label.text}:"
                                confidenceOne.text = label.confidence.toString() }
                        2 -> {  labelTwo.text = "${label.text}:"
                                confidenceTwo.text = label.confidence.toString() }
                        3 -> {  labelThree.text = "${label.text}:"
                                confidenceThree.text = label.confidence.toString() }
                    }
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "failure")
            }

        progressBar.stop()
    }

    private fun loadGif(uri: Uri?) {
        progressBar.start()

        Glide.with(this)
            .asGif()
            .load(uri)
            .override(imagedisplay_image.width, imagedisplay_image.width / 2)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstRes: Boolean) = false

                override fun onResourceReady(res: GifDrawable?, model: Any?, target: Target<GifDrawable>?,
                    source: DataSource?, isFirstRes: Boolean): Boolean {
                    progressBar.stop()
                    return false
                }
            })
            .into(image)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.imagedisplay_image)
    fun showAIInfo(){
        if (isToolbarVisible) {
            toolbar.visibility = View.INVISIBLE
            layout.visibility = View.INVISIBLE
        } else {
            toolbar.visibility = View.VISIBLE
            layout.visibility = View.VISIBLE
        }

        isToolbarVisible = !isToolbarVisible
    }

    @OnClick(R.id.imagedisplay_download)
    fun downloadImage(){
        val randomNum = (Math.random() * 100000 + 1).toInt()
        bitmap?.let {
            val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "IMAGE${randomNum}", "")
            Uri.parse(path)

            toast("Image saved")
        }

        finish()
    }
}