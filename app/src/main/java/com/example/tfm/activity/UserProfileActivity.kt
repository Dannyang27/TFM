package com.example.tfm.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.model.User
import com.example.tfm.util.*
import com.example.tfm.viewmodel.UserProfileViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

class UserProfileActivity : AppCompatActivity() {

    @BindView(R.id.profile_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.user_profile) lateinit var profilePhoto: CircleImageView
    @BindView(R.id.profile_fab) lateinit var fab: FloatingActionButton
    @BindView(R.id.profile_username_layout) lateinit var usernameLayout: LinearLayout
    @BindView(R.id.profile_username) lateinit var username: TextView
    @BindView(R.id.profile_status_layout) lateinit var statusLayout: LinearLayout
    @BindView(R.id.profile_status) lateinit var status: TextView
    @BindView(R.id.profile_email) lateinit var email: TextView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var userProfileViewModel: UserProfileViewModel
    private val GALLERY_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        ButterKnife.bind(this)

        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)

        firestore = FirebaseFirestore.getInstance()
        toolbar.title = getString(R.string.profile)
        displayArrowBack(toolbar)

        userProfileViewModel.getUser().observe(this, Observer { user ->
            if(!user.profilePhoto.isNullOrEmpty()){
                Glide.with(this).load(user.profilePhoto.toBitmap()).into(profilePhoto)
            }

            username.text = user.name
            status.text = user.status
            email.text = user.email
            DataRepository.user = user
        })
    }

    private fun displayArrowBack(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun loadImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            GALLERY_REQUEST_CODE -> {
                if(data != null && resultCode == Activity.RESULT_OK){
                    val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data) as Bitmap
                    val bitmap = originalBitmap.createNewBitmap()

                    CoroutineScope(Dispatchers.IO).launch {
                        val task = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(DataRepository.currentUserEmail).get().await()
                        var user = task.toObject(User::class.java)?.copy(profilePhoto = bitmap.toBase64())!!
                        firestore.updateCurrentUser(this@UserProfileActivity, user)
                        userProfileViewModel.updateUser(user)
                    }
                }
            }
        }
    }

    private fun showDialog(activity: Activity, isUsername: Boolean){
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_enter_input)

        val input = dialog.findViewById<EditText>(R.id.dialog_input)
        val titleText = dialog.findViewById<TextView>(R.id.enter_text)
        val acceptBtn = dialog.findViewById<Button>(R.id.dialog_accept)
        val cancelBtn = dialog.findViewById<Button>(R.id.dialog_cancel)

        if(isUsername){
            input.text.append(profile_username.text)
            titleText.text = getString(R.string.enter_username)
        }else{
            input.text.append(profile_status.text)
            titleText.text = getString(R.string.enter_status)
        }

        acceptBtn.setOnClickListener {
            if(input.text.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch{
                    val task = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(DataRepository.currentUserEmail).get().await()
                    var user: User
                    if(isUsername){
                        user = task.toObject(User::class.java)?.copy(name = input.text.toString())!!
                    }else{
                        user = task.toObject(User::class.java)?.copy(status = input.text.toString())!!
                    }

                    firestore.updateCurrentUser(activity, user)
                    userProfileViewModel.updateUser(user)
                }

                dialog.dismiss()
            }else{
                toast("Input can't be empty")
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.profile_fab)
    fun fabClick(){ loadImage() }

    @OnClick(R.id.profile_username_layout)
    fun usernameClick(){
        showDialog(this, true)
    }

    @OnClick(R.id.profile_status_layout)
    fun statusClick(){
        showDialog(this, false)
    }
}