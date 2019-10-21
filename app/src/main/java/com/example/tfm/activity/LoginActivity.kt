package com.example.tfm.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.model.User
import com.example.tfm.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Default + job

    private lateinit var prefs: SharedPreferences
    private var auth: FirebaseAuth? = null

    companion object{
        lateinit var progressBar: ProgressBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.login_progressbar)

        auth = FirebaseAuth.getInstance()

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val (emailPref, passwordPref) = prefs.getCredentials()

        if(emailPref.isNotEmpty() && passwordPref.isNotEmpty()){
            loginUser(emailPref.trimBothSides(), passwordPref)
        }

        login_signup_button.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        login_button.setOnClickListener {
            if(login_email.text.isNotEmpty() && login_password.text.isNotEmpty()){
                loginUser( login_email.text.toString().trimBothSides(), login_password.text.toString())
            }
        }
    }

    private fun loginUser(user: String, password: String){
        progressBar.start()
        disableViews()
        auth?.signInWithEmailAndPassword(user, password)
            ?.addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(LogUtil.TAG, "Signed in successfully")
                    prefs.updateCurrentUser(user, password)

                    launch {
                        DataRepository.initTranslator(this@LoginActivity)
                        FirebaseUtil.loadUserConversation(this@LoginActivity, user)
                        val task = FirebaseFirestore.getInstance().collection(FirebaseUtil.FIREBASE_USER_PATH).document(DataRepository.currentUserEmail).get().await()
                        DataRepository.user = task.toObject(User::class.java)
                    }

                }else{
                    toast("Wrong user/password")
                    enableViews()
                    progressBar.stop()
                }
            }
    }

    private fun disableViews(){
        login_email.isEnabled = false
        login_password.isEnabled = false
        login_signup_button.isEnabled = false
        login_button.isEnabled = false
    }

    private fun enableViews(){
        login_email.isEnabled = true
        login_password.isEnabled = true
        login_signup_button.isEnabled = true
        login_button.isEnabled = true
    }
}

