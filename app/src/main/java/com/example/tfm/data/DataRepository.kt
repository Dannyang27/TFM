package com.example.tfm.data

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.tfm.model.User
import com.example.tfm.util.LogUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.*

object DataRepository{
    
    var fromEnglishTranslator: FirebaseTranslator? = null
    var toEnglishTranslator: FirebaseTranslator? = null
    var languagePreferenceCode: Int? = null
    var user: User? = null
    var currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

    var conversationPositionClicked: Int = -1

    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO)

    const val CAMERA_PERMISSION = 1
    const val STORAGE_PERMISSION = 2
    const val LOCATION_PERMISSION = 3
    const val AUDIO_PERMISSION = 4

    fun initTranslator(context: Context){
        val language  = PreferenceManager.getDefaultSharedPreferences(context).getString("chatLanguage", "English").toString()

        //if default, do not start creating models
        if(language == "English"){
            return
        }

        val code = com.example.tfm.util.FirebaseTranslator.languageCodeFromString(language)
        val model = FirebaseTranslateRemoteModel.Builder(code).build()

        languagePreferenceCode = code

        val modelManager = FirebaseTranslateModelManager.getInstance()
        modelManager.getAvailableModels(FirebaseApp.getInstance())
            .addOnSuccessListener { modelSet ->
                if (modelSet.contains(model)) {
                    val options = FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(code)
                        .build()

                    val optionsTwo = FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(code)
                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
                        .build()

                    val firebaseNaturalLanguage = FirebaseNaturalLanguage.getInstance()
                    fromEnglishTranslator = firebaseNaturalLanguage.getTranslator(options)
                    toEnglishTranslator = firebaseNaturalLanguage.getTranslator(optionsTwo)
                }else{
                    Log.d(LogUtil.TAG, "Model doesnt exist")
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Failed while initialising model")
            }
    }

    fun appIsInBackground(): Boolean {
        val myProcess = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(myProcess)
        return myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }
}