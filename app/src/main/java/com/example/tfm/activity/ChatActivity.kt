package com.example.tfm.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.emoji.text.EmojiCompat
import androidx.emoji.widget.EmojiEditText
import androidx.core.app.ActivityCompat
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.FileProvider
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.fragments.EmojiFragment
import com.example.tfm.fragments.GifFragment
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.util.KeyboardUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.util.*

class ChatActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var container: FrameLayout
    private lateinit var bottomNavBar: BottomNavigationView

    private val GALLERY_CODE = 100
    private val CAMERA_MODE = 101
    private val ATTACHMENT_MODE = 102

    private var currentPhotoPath: String? = null

    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object{
        var messages = mutableListOf<Message>()
        lateinit var messagesRecyclerView: RecyclerView
        lateinit var emojiEditText: EmojiEditText
        lateinit var viewAdapter : RecyclerView.Adapter<*>

        val emojiFragment = EmojiFragment.newInstance()
        val gifFragment = GifFragment.newInstance()
        var activeFragment: Fragment = emojiFragment

        fun sendMessage(message: Message){
            messages.add(message)
            viewAdapter.notifyDataSetChanged()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.emoji_navbar -> {
                openFragment(emojiFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.gif_navbar -> {
                openFragment(gifFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEmoji()
        setContentView(R.layout.activity_chat)
        setSupportActionBar(chat_toolbar)
        displayBackArrow()

        emojiEditText = findViewById(R.id.chat_edittext)

        container = findViewById(R.id.emoji_container)
        bottomNavBar = findViewById(R.id.emoji_navbar)

        bottomNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, gifFragment, "2").hide(gifFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, emojiFragment, "1").commit()

        initListeners()

        //sample messages
        messages.add(Message(Sender.OWN, MessageType.MESSAGE, "Hello World",  1212, "EN" ))
        messages.add(Message(Sender.OTHER, MessageType.MESSAGE, getString(R.string.dwight_quote), 1213 , "EN"))

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        viewAdapter = ChatAdapter(messages, this)

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
        KeyboardUtil.hideKeyboard(this)
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
        emojiEditText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        closeSpecialKeyboard()
                        KeyboardUtil.showKeyboard(this@ChatActivity)
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        emojiButton.setOnClickListener {
            showSpecialKeyboard()
        }
        pictureButton.setOnClickListener {
            closeSpecialKeyboard()
            openGallery()
        }
        cameraButton.setOnClickListener {
            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }else{
                closeSpecialKeyboard()
                dispatchCameraIntent()
            }
        }
        micButton.setOnClickListener {
            toast("Microphone")
            closeSpecialKeyboard()
        }
        locationButton.setOnClickListener {
            closeSpecialKeyboard()

            val locationIntent = Intent(this, LocationSenderActivity::class.java)
            startActivity(locationIntent)
        }
        attachmentButton.setOnClickListener {
            openAttachment()
            closeSpecialKeyboard()
        }
        codeButton.setOnClickListener {
            toast("Code")
            closeSpecialKeyboard()
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

        if(resultCode != RESULT_CANCELED){
            when(requestCode){
                GALLERY_CODE ->{
                    data.let{ ImageSenderActivity.launchActivityWithImage(this, it?.data, MediaSource.GALLERY) }
                }

                ATTACHMENT_MODE -> {
                    data.let{
                        val uploadFileUri = it?.data
                        val file = File(uploadFileUri?.path)
                        toast(file.absolutePath)
                    }
                }

                CAMERA_MODE -> {
                    if(resultCode == RESULT_OK){
                        ImageSenderActivity.launchActivityWithImage(this, Uri.parse(currentPhotoPath), MediaSource.CAMERA)
                    }
                }

                else -> toast("Other")
            }
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

    private fun openAttachment(){
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            val attachIntent = Intent(Intent.ACTION_GET_CONTENT)
            attachIntent.type = "*/*"
            startActivityForResult(Intent.createChooser(attachIntent, "Select file"), ATTACHMENT_MODE)
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onBackPressed() {
        if(container.visibility == View.VISIBLE){
            closeSpecialKeyboard()
        }else{
            super.onBackPressed()
        }
    }

    private fun closeSpecialKeyboard(){
        container.visibility = View.GONE
        bottomNavBar.visibility = View.GONE
    }

    private fun showSpecialKeyboard(){
        KeyboardUtil.hideKeyboard(this)

        launch {
            // avoids overslapping from soft keyboard and emoji keyboard
            delay(50L)
            withContext(Dispatchers.Main) {
                container.visibility = View.VISIBLE
                bottomNavBar.visibility = View.VISIBLE
            }
        }
    }

    private fun dispatchCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.tfm.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_MODE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
}