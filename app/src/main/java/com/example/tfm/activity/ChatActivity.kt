package com.example.tfm.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.bundled.BundledEmojiCompatConfig
import android.support.text.emoji.widget.EmojiEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var emojiEditText: EmojiEditText

    var currentPhotoPath: String = ""

    private val GALLERY_CODE = 100
    private val CAMERA_MODE = 101

    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object{
        var messages = mutableListOf<Message>()
        lateinit var messagesRecyclerView: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEmoji()
        setContentView(R.layout.activity_chat)
        setSupportActionBar(chat_toolbar)
        displayBackArrow()

        emojiEditText = findViewById(R.id.chat_edittext)


        initListeners()

        //sample messages
        messages.add(Message(Sender.OWN, MessageType.MESSAGE, "Hello World",  1212, "EN" ))
        messages.add(Message(Sender.OTHER, MessageType.MESSAGE, getString(R.string.dwight_quote), 1213 , "EN"))
        //messages.add(Message(Sender.OTHER, MessageType.PHOTO, "", 1214 , "EN"))

        viewManager = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(messages)

        messagesRecyclerView = findViewById<RecyclerView>(R.id.chat_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if(bottom < oldBottom){
                    postDelayed({ scrollToPosition(viewAdapter.itemCount - 1) }, 0)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {

        R.id.call -> {
            toast("Phonecalling...")
            true
        }

        R.id.videocall -> {
            toast("Videocalling...")
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun displayBackArrow(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initEmoji(){
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    private fun initListeners(){
        emojiButton.setOnClickListener {
            toast("Emoji")
        }
        pictureButton.setOnClickListener {
            toast("Photo")
            openGallery()
        }
        gifButton.setOnClickListener {
            toast("GIF")
        }
        cameraButton.setOnClickListener {
            toast("Camera")
            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }else{
                openCamera()
            }
        }
        micButton.setOnClickListener {
            toast("Microphone")
        }
        locationButton.setOnClickListener {
            toast("Location")
        }
        attachmentButton.setOnClickListener {
            toast("Attachment")
        }
        codeButton.setOnClickListener {
            toast("Code")
        }

        sendButton.setOnClickListener {
            val fieldText = chat_edittext.text.toString()
            if(fieldText.isNotEmpty()){
                messages.add(Message(Sender.OWN, MessageType.MESSAGE, fieldText, 1, "EN" ))
                messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
                viewAdapter.notifyDataSetChanged()
                chat_edittext.text.clear()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            GALLERY_CODE ->{
                if(data != null){
                    val uri = data.data
                    val filePath : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(uri, filePath, null, null, null)
                    if(cursor.moveToFirst()){
                        val columnIndex = cursor.getColumnIndex(filePath[0])
                        val fileP = cursor.getString(columnIndex)
                        val imageBitmap = BitmapFactory.decodeFile(fileP)

                        messages.add(Message(Sender.OWN, MessageType.PHOTO, imageBitmap, 1 , "EN"))
                        messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
                        viewAdapter.notifyDataSetChanged()
                    }
                    cursor.close()
                }else{
                    toast("No image loaded")
                }
            }

            CAMERA_MODE -> {
                if(data != null){
                    val imageBitmap = data.extras.get("data") as Bitmap

//                    val file = File(currentPhotoPath)
//                    val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
//
//
                    messages.add(Message(Sender.OWN, MessageType.PHOTO, imageBitmap, 1 , "EN"))
                    messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
                    viewAdapter.notifyDataSetChanged()
                }else{
                    toast("Could not get any shot")
                }
            }

            else -> toast("Other")
        }
    }

    private fun openGallery(){
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_CODE)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.tfm.fileprovider",
                        it
                    )

                    Log.d("PHOTO", photoURI.path)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_MODE)
                }
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}
