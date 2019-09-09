package com.example.tfm.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MediaSource
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.updateCurrentUser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

class UserProfileActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()

    private lateinit var usernameLayout: LinearLayout
    private lateinit var statusLayout: LinearLayout
    private lateinit var username: TextView
    private lateinit var status: TextView
    private lateinit var email: TextView
    private lateinit var fab: FloatingActionButton

    private lateinit var firestore: FirebaseFirestore

    private val GALLERY_REQUEST_CODE = 100

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        firestore = FirebaseFirestore.getInstance()
        toolbar = findViewById(R.id.profile_toolbar)
        toolbar.title = getString(R.string.profile)

        usernameLayout = findViewById(R.id.profile_username_layout)
        statusLayout = findViewById(R.id.profile_status_layout)
        username = findViewById(R.id.profile_username)
        status = findViewById(R.id.profile_status)
        email = findViewById(R.id.profile_email)
        fab = findViewById(R.id.profile_fab)

        fab.setOnClickListener {
            toast("Changing profile photo...")
            loadImage()
        }

        usernameLayout.setOnClickListener {
            showDialog(this, true)
        }

        statusLayout.setOnClickListener {
            showDialog(this, false)
        }

        displayArrowBack(toolbar)
        launch { initUserProfile() }
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
                data?.let {ImageToolActivity.launchImageTool(this, it.data, MediaSource.GALLERY, true) }
            }
        }
    }

    private fun initUserProfile(){
        val user = DataRepository.user
        username.text = user?.name ?: "N?A"
        status.text = user?.status ?: "N/A"
        email.text = user?.email ?: "N/A"
    }

    private fun showDialog(activity: Activity, isUsername: Boolean){
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_enter_input)

        val input = dialog.findViewById<EditText>(R.id.dialog_input).apply {
            text.append(if (isUsername) username.text else status.text)
        }
        val acceptBtn = dialog.findViewById<Button>(R.id.dialog_accept)
        val cancelBtn = dialog.findViewById<Button>(R.id.dialog_cancel)

        acceptBtn.setOnClickListener {
            if(input.text.isNotEmpty()){
                launch{
                    val task = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(DataRepository.currentUserEmail).get().await()

                    if(isUsername){
                        var user = task.toObject(User::class.java)?.copy(name = input.text.toString())!!
                        firestore.updateCurrentUser(activity, user, input.text.toString(), username)
                    }else{
                        var user = task.toObject(User::class.java)?.copy(status = input.text.toString())!!
                        firestore.updateCurrentUser(activity, user, input.text.toString(), status)
                    }
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
}
