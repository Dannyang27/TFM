package com.example.tfm.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    fun showKeyboard(activity: Activity){
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 1 )
    }

    fun hideKeyboard(activity: Activity){
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0 )
    }
}