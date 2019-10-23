package com.example.tfm.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.emoji.widget.EmojiEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.fragments.EmojiFragment
import com.example.tfm.fragments.GifFragment
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.FirebaseTranslator
import com.example.tfm.util.KeyboardUtil
import com.example.tfm.util.toBitmap
import com.example.tfm.viewmodel.ChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.util.*

class ChatActivity : AppCompatActivity(){

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val emojiFragment = EmojiFragment.newInstance()
    private val gifFragment = GifFragment.newInstance()
    private var activeFragment: Fragment = emojiFragment

    private val GALLERY_CODE = 100
    private val CAMERA_MODE = 101
    private val ATTACHMENT_MODE = 102

    private var currentPhotoPath: String? = null
    private var translateModel: String? = null

    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO)

    companion object{
        lateinit var emojiEditText: EmojiEditText
        lateinit var messagesRecyclerView: RecyclerView
        lateinit var viewAdapter : ChatAdapter
        lateinit var conversationId: String
        lateinit var receiverUser: String

        private fun scrollToBottom(){
            messagesRecyclerView.scrollToPosition(viewAdapter.itemCount - 1)
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

        emojiEditText = findViewById(R.id.chat_edittext)
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        conversationId = intent.getStringExtra("conversationId")
        receiverUser = intent.getStringExtra("receiverEmail")
        val username = intent.getStringExtra("receiverName")
        val photo = intent.getStringExtra("profilePhoto")

        setToolbar(username, photo)

        chatViewModel.getLanguageFlag().observe(this, Observer {drawable ->
            chat_flag.setImageDrawable(getDrawable(drawable))
        })

        chatViewModel.getTranslatedModel().observe(this, Observer {model->
            translateModel = model
        })

        chatViewModel.getShowEmojiKeyboard().observe(this, Observer { showKeyboard ->
            if(showKeyboard){
                showSpecialKeyboard()
            }else{
                closeSpecialKeyboard()
            }
        })

        chatViewModel.getChatMessages().observe(this, Observer {
            viewAdapter.updateList(it)
        })

        viewManager = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(mutableListOf(), this)

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

        emoji_navbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, gifFragment, "2").hide(gifFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, emojiFragment, "1").commit()

        chatViewModel.initLanguageFlag(this)
        chatViewModel.initMessages(conversationId)
        initListeners()
//        messages.clear()

//        val conversationMessages = DataRepository.getConversation(conversationId)?.messages
//
//        val pref = PreferenceManager.getDefaultSharedPreferences(this).getLanguage()
//        if(pref == "Default"){
//            updateList(conversationMessages!!)
//        }else{
//            val conversationTranslated = mutableListOf<Message>()
//
//            val translator = DataRepository.fromEnglishTranslator
//
//            CoroutineScope(Dispatchers.IO).launch {
//                conversationMessages.let{
//                    it?.forEach { message ->
//                        if(MessageType.fromInt(message.messageType) == MessageType.MESSAGE){
//                            val isLanguagePreference = message.body?.fieldThree.toString().isUserLanguagePreference()
//                            if(!isLanguagePreference){
//                                translator?.translate(message.body?.fieldTwo.toString())
//                                    ?.addOnSuccessListener { translatedText ->
//                                        val m = message.copy( body = MessageContent(message.body?.fieldOne.toString(), translatedText, message.body?.fieldThree.toString()))
//                                        conversationTranslated.add(m)
//                                        updateList(conversationTranslated)
//                                    }
//                            }else{
//                                conversationTranslated.add(message)
//                                updateList(conversationTranslated)
//                            }
//                        }else{
//                            conversationTranslated.add(message)
//                            updateList(conversationTranslated)
//                        }
//                    }
//                }
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        KeyboardUtil.hideKeyboard(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun setToolbar(username: String, photo: String){
        setSupportActionBar(chat_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        chat_toolbar_title.text = username

        try{
            Glide.with(this).load(photo.toBitmap()).into(chat_profile_image)
        }catch (e: Exception){
            Log.d("TFM", "Error while loading profile photo, maybe theres no photo")
        }
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
        emojiEditText.setOnTouchListener { view , event ->
            when(event?.action){
                MotionEvent.ACTION_UP -> {
                    chatViewModel.showKeyboard(false)
                    KeyboardUtil.showKeyboard(this@ChatActivity)
                }
            }
            view?.onTouchEvent(event ) ?: true
        }

        emojiButton.setOnClickListener {
            chatViewModel.showKeyboard(true)
        }
        pictureButton.setOnClickListener {
            chatViewModel.showKeyboard(false)
            openGallery()
        }
        cameraButton.setOnClickListener {
            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }else{
                chatViewModel.showKeyboard(false)
                dispatchCameraIntent()
            }
        }
        micButton.setOnClickListener {
            toast("Microphone")
            chatViewModel.showKeyboard(false)
        }
        locationButton.setOnClickListener {
            chatViewModel.showKeyboard(false)
            startActivity(Intent(this, LocationSenderActivity::class.java))
        }
        attachmentButton.setOnClickListener {
            openAttachment()
            chatViewModel.showKeyboard(false)
        }
        codeButton.setOnClickListener {
            toast("Code")
            chatViewModel.showKeyboard(false)
        }

        sendButton.setOnClickListener {
            val fieldText = chat_edittext.text.toString()
            if(fieldText.isNotEmpty()){
                val languageCode = FirebaseTranslator.languageCodeFromString(translateModel.toString())
                val timestamp = System.currentTimeMillis()
                val message = Message(timestamp, conversationId, DataRepository.currentUserEmail, receiverUser,
                    MessageType.MESSAGE.value, MessageContent(fieldOne = fieldText, fieldThree = languageCode.toString()), timestamp )

                ChatViewModel.addMessage(message)
                chat_edittext.text.clear()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != RESULT_CANCELED){
            when(requestCode){
                GALLERY_CODE ->{
                    data.let{ ImageToolActivity.launchImageTool(this, it?.data, MediaSource.GALLERY) }
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
                        ImageToolActivity.launchImageTool(this, Uri.parse(currentPhotoPath),  MediaSource.CAMERA)
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
        if(emoji_container.visibility == View.VISIBLE){
            chatViewModel.showKeyboard(false)
        }else{
            supportFragmentManager.beginTransaction().remove(emojiFragment).commit()
            supportFragmentManager.beginTransaction().remove(gifFragment).commit()
            super.onBackPressed()
        }
    }

    private fun closeSpecialKeyboard(){
        emoji_container.visibility = View.GONE
        emoji_navbar.visibility = View.GONE
    }

    private fun showSpecialKeyboard(){
        KeyboardUtil.hideKeyboard(this)

        CoroutineScope(Dispatchers.IO).launch {
            // avoids overslapping from soft keyboard and emoji keyboard
            delay(50L)
            withContext(Dispatchers.Main) {
                emoji_container.visibility = View.VISIBLE
                emoji_navbar.visibility = View.VISIBLE
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
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
}