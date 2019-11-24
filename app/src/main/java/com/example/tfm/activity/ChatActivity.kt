package com.example.tfm.activity

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.data.DataRepository
import com.example.tfm.data.DataRepository.PERMISSIONS
import com.example.tfm.enum.LanguageCode
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.fragments.EmojiFragment
import com.example.tfm.fragments.GifFragment
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.*
import com.example.tfm.viewmodel.ChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.util.*

class ChatActivity : AppCompatActivity(){

    @BindView(R.id.chat_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.chat_toolbar_title) lateinit var tvToolbar: TextView
    @BindView(R.id.chat_profile_image) lateinit var profileImg: CircleImageView
    @BindView(R.id.chat_flag) lateinit var flag: ImageView
    @BindView(R.id.chat_recyclerview) lateinit var rvMessages: RecyclerView
    @BindView(R.id.chat_sendButton) lateinit var bSend: ImageButton
    @BindView(R.id.emojiButton) lateinit var bEmoji: ImageButton
    @BindView(R.id.pictureButton) lateinit var bPicture: ImageButton
    @BindView(R.id.cameraButton) lateinit var bCamera: ImageButton
    @BindView(R.id.micButton) lateinit var bMic: ImageButton
    @BindView(R.id.locationButton) lateinit var bLocation: ImageButton
    @BindView(R.id.attachmentButton) lateinit var bAttachment: ImageButton
    @BindView(R.id.codeButton) lateinit var bCode: ImageButton
    @BindView(R.id.emoji_navbar) lateinit var navbar: BottomNavigationView

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter : ChatAdapter
    private val emojiFragment = EmojiFragment.newInstance()
    private val gifFragment = GifFragment.newInstance()
    private var activeFragment: Fragment = emojiFragment

    private val GALLERY_CODE = 100
    private val CAMERA_MODE = 101
    private val ATTACHMENT_MODE = 102

    private var currentPhotoPath: String? = null
    private var translateModel: String? = null

    private val PERMISSION_ALL = 1

    companion object{
        lateinit var emojiEditText: EmojiEditText
        lateinit var conversationId: String
        lateinit var receiverUser: String
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
        ButterKnife.bind(this)

        emojiEditText = findViewById(R.id.chat_edittext)
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        conversationId = intent.getStringExtra("conversationId")
        receiverUser = intent.getStringExtra("receiverEmail")
        val username = intent.getStringExtra("receiverName")
        val photo = intent.getStringExtra("profilePhoto")

        setToolbar(username, photo)

        chatViewModel.getLanguageFlag().observe(this, Observer {drawable ->
            flag.setImageDrawable(getDrawable(drawable))
        })

        chatViewModel.getTranslatedModel().observe(this, Observer {model->
            translateModel = model
        })

        chatViewModel.getShowEmojiKeyboard().observe(this, Observer { showKeyboard ->
            if(showKeyboard){ showSpecialKeyboard()
            }else{ closeSpecialKeyboard() }
        })

        chatViewModel.getChatMessages().observe(this, Observer {
            viewAdapter.updateList(it)
            scrollToBottom()
        })

        viewManager = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(mutableListOf(), this)

        setFragments()
        chatViewModel.initLanguageFlag(this)
        chatViewModel.initRoomObserver(this, conversationId)

        rvMessages.apply {
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
        KeyboardUtil.hideKeyboard(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun setFragments(){
        navbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, gifFragment, "2").hide(gifFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.emoji_container, emojiFragment, "1").commit()
    }

    private fun setToolbar(username: String?, photo: String?){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        tvToolbar.text = username

        try{
            Glide.with(this).load(photo?.toBitmap()).into(profileImg)

            profileImg.setOnClickListener {
                chat_profile_image.showDialog(this, photo)
            }

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

    private fun scrollToBottom(){
        rvMessages.scrollToPosition(viewAdapter.itemCount - 1)
    }

    @OnClick(R.id.chat_sendButton)
    fun sendMessage(){
        val fieldText = chat_edittext.text.toString()
        if(fieldText.isNotEmpty()){
            val languageCode = FirebaseTranslator.languageCodeFromString(translateModel.toString())
            val timestamp = System.currentTimeMillis()

            val message = Message(timestamp, conversationId, DataRepository.currentUserEmail, receiverUser,
                MessageType.MESSAGE.value, null, timestamp )

            if(languageCode == LanguageCode.ENGLISH.code){
                message.body = MessageContent(fieldText, "", languageCode.toString())
                chatViewModel.saveMessage(message)

            }else{
                val translator = DataRepository.toEnglishTranslator

                translator?.let{
                    it.translate(fieldText).addOnSuccessListener {translatedText ->
                    message.body = MessageContent(fieldText, translatedText, languageCode.toString())
                    chatViewModel.saveMessage(message)

                }.addOnFailureListener {
                    Log.d("TFM", "Cannot translate")
                }
                }
            }

            chat_edittext.text.clear()
        }
    }

    @OnClick(R.id.emojiButton)
    fun openEmoji(){
        chatViewModel.showKeyboard(true)
    }

    @OnClick(R.id.pictureButton)
    fun operImages(){
        chatViewModel.showKeyboard(false)
        if(!checkPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_CODE)
        }
    }

    @OnClick(R.id.cameraButton)
    fun openCamera(){
        if(!checkPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            chatViewModel.showKeyboard(false)
            dispatchCameraIntent()
        }
    }

    @OnClick(R.id.micButton)
    fun openMic(){
        toast("Microphone")
        chatViewModel.showKeyboard(false)
    }

    @OnClick(R.id.locationButton)
    fun openLocation(){
        if(!checkPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            chatViewModel.showKeyboard(false)
            startActivity(Intent(this, LocationSenderActivity::class.java))
        }
    }

    @OnClick(R.id.attachmentButton)
    fun openAttachment(){
        if(!checkPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }else{
            val attachIntent = Intent(Intent.ACTION_GET_CONTENT)
            attachIntent.type = "*/*"
            startActivityForResult(Intent.createChooser(attachIntent, "Select file"), ATTACHMENT_MODE)
        }

        chatViewModel.showKeyboard(false)
    }

    @OnClick(R.id.codeButton)
    fun openCode(){
        toast("Code")
        chatViewModel.showKeyboard(false)
    }

    @OnTouch(R.id.chat_edittext)
    fun touched(view: View?, event: MotionEvent?){
        when(event?.action){
            MotionEvent.ACTION_UP -> {
                chatViewModel.showKeyboard(false)
                KeyboardUtil.showKeyboard(this@ChatActivity)
            }
        }
        view?.onTouchEvent(event )
    }
}