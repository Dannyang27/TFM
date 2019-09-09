package com.example.tfm.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.tfm.R

class MySettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
    }
}