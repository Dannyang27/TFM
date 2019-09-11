package com.example.tfm.preference

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tfm.R
import com.example.tfm.activity.PrivacyPolicyActivity
import com.example.tfm.enum.LanguageCode
import com.example.tfm.util.FirebaseTranslator
import com.example.tfm.util.LogUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateModelManager
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import org.jetbrains.anko.toast

class MySettingFragment : PreferenceFragmentCompat(), CoroutineScope{
    override val coroutineContext = Dispatchers.Default + Job()
    private lateinit var languagePreference: ListPreference
    private lateinit var dialog: Dialog
    private var isModelDownloaded = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)

        languagePreference =  findPreference("chatLanguage") as ListPreference
        val deleteModelPreference = findPreference("deleteLanguageModels") as Preference
        val privacyPreference = findPreference("termsAndConditions") as Preference

        languagePreference.setOnPreferenceChangeListener { _, newValue ->
            Log.d(LogUtil.TAG, "Language selected: $newValue")
            showDialog(languagePreference.context)
            isModelDownloaded = false
            downloadLanguageModels(newValue.toString())
            true
        }

        deleteModelPreference.setOnPreferenceClickListener {
            deleteLanguageModels()
            true
        }

        privacyPreference.setOnPreferenceClickListener {
            activity?.startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
            true
        }
    }


    private fun downloadLanguageModels(fromLanguage: String){
        val targetLanguage = FirebaseTranslator.languageCodeFromString(fromLanguage)

        async {
            downloadLanguageModel(LanguageCode.ENGLISH.code, targetLanguage)
            downloadLanguageModel(targetLanguage, LanguageCode.ENGLISH.code)
        }
    }

    private fun downloadLanguageModel(fromLanguage: Int, toLanguage: Int){
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(fromLanguage)
            .setTargetLanguage(toLanguage)
            .build()

        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                if(isModelDownloaded){
                    dialog.dismiss()
                    activity?.toast(getString(R.string.restartapp))
                }else{
                    isModelDownloaded = true
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Failed model")
            }
    }

    private fun deleteLanguageModels(){
        val modelManager = FirebaseTranslateModelManager.getInstance()

        modelManager.getAvailableModels(FirebaseApp.getInstance())
            .addOnSuccessListener {
                it.forEach { model ->
                    modelManager.deleteDownloadedModel(model)
                        .addOnSuccessListener {
                            Log.d(LogUtil.TAG, "Models $model deleted")
                        }
                        .addOnFailureListener {
                            Log.d(LogUtil.TAG, "Error while deleting model")
                        }
                }

                languagePreference.setValueIndex(0)
                activity?.toast(getString(R.string.modelsdeleted))
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Failed while getting all language models")
            }
    }

    private fun showDialog(context: Context){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_progressbar)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}