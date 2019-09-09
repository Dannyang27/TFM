package com.example.tfm.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.util.loadImageFromUri
import com.example.tfm.util.rotate
import com.example.tfm.util.setBitmapToImageView
import org.jetbrains.anko.toast

class ImageToolActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var rotateBtn: ImageButton
    private lateinit var acceptBtn: Button

    companion object{
        fun launchImageTool(context: Context, uri: Uri?){
            val intent = Intent(context, ImageToolActivity::class.java)
            intent.putExtra("imageUrl", uri?.toString())
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_tool)

        val uri = intent.getStringExtra("imageUrl")

        image = findViewById(R.id.tool_image)
        cancelBtn = findViewById(R.id.tool_cancel)
        rotateBtn = findViewById(R.id.tool_rotate)
        acceptBtn = findViewById(R.id.tool_accept)

        val bitmap = loadImageFromUri(uri)
        setBitmapToImageView(image, bitmap)

        cancelBtn.setOnClickListener {
            finish()
        }

        rotateBtn.setOnClickListener {
            image.rotate()
        }

        acceptBtn.setOnClickListener {
            toast("Setting new profile photo")
        }
    }
}
