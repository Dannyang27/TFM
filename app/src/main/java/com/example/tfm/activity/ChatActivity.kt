package com.example.tfm.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.tfm.speechrecognizer.MyRecognitionListener
import com.example.tfm.speechrecognizer.VoiceRecognitionFactory
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
    @BindView(R.id.chat_flag) lateinit var flag: TextView
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
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter : ChatAdapter
    private val emojiFragment = EmojiFragment.newInstance()
    private val gifFragment = GifFragment.newInstance()
    private var activeFragment: Fragment = emojiFragment

    private lateinit var speaker: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private val GALLERY_CODE = 100
    private val CAMERA_MODE = 101
    private val ATTACHMENT_MODE = 102
    private var isSpeakerInit = false

    private var currentPhotoPath: String? = null
    private var translateModel: String? = null

    private val PERMISSION_ALL = 1

    private var firstLoadingCanScroll = false

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

        chatViewModel.getLanguageFlag().observe(this, Observer { flagCode ->
            flag.text = flagCode.toUpperCase()
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

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if(!canScrollVertically(-1)){
                        if(!firstLoadingCanScroll && canScrollVertically(0)){
                            viewManager.stackFromEnd = true
                            firstLoadingCanScroll = true
                        }else{
                            Log.d(LogUtil.TAG, "Top reached, load more messages...")
                        }
                    }
                }
            })
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

    private fun initSpeaker(){
        speaker = SpeechRecognizer.createSpeechRecognizer(this)
        speaker.setRecognitionListener(MyRecognitionListener())
        val userLanguageCode = LanguageCode.getLanguageBCP47Code(DataRepository.languagePreferenceCode)
        recognizerIntent = VoiceRecognitionFactory.createSpeechRecognizer(userLanguageCode)

        isSpeakerInit = !isSpeakerInit
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
                chatViewModel.sendMessage(message)
            }else{
                val translator = DataRepository.toEnglishTranslator

                translator?.let{
                        it.translate(fieldText).addOnSuccessListener { translatedText ->
                            message.body = MessageContent(fieldText, translatedText, languageCode.toString())
                            chatViewModel.sendMessage(message)
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
    fun galleryBtn(){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                DataRepository.STORAGE_PERMISSION)
        }else{
            openGallery()
        }
    }

    @OnClick(R.id.cameraButton)
    fun cameraBtn(){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                DataRepository.CAMERA_PERMISSION)
        }else{
            openCamera()
        }
    }

    @OnClick(R.id.micButton)
    fun micBtn(){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                DataRepository.AUDIO_PERMISSION)
        }else{
            openMic()
        }
    }

    @OnClick(R.id.locationButton)
    fun locationBtn(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                DataRepository.LOCATION_PERMISSION)
        }else{
            openLocation()
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

    private fun openGallery(){
        chatViewModel.showKeyboard(false)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_CODE)
    }

    private fun openCamera(){
        chatViewModel.showKeyboard(false)
        dispatchCameraIntent()
    }

    private fun openMic(){
        chatViewModel.showKeyboard(false)

        if(!isSpeakerInit){
            initSpeaker()
        }

        speaker.startListening(recognizerIntent)
    }

    private fun openLocation(){
        chatViewModel.showKeyboard(false)
        startActivity(Intent(this, LocationSenderActivity::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when(requestCode){
                DataRepository.CAMERA_PERMISSION -> {
                    openCamera()
                }

                DataRepository.STORAGE_PERMISSION -> {
                    openGallery()
                }

                DataRepository.LOCATION_PERMISSION -> {
                    openLocation()
                }

                DataRepository.AUDIO_PERMISSION -> {
                    openMic()
                }
            }
        }
    }
}