package com.example.tfm.util

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.adapter.EmojiGridViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

fun TextView.showUsernameIfGroup( isPrivateChat: Boolean, username: String){
    if(isPrivateChat) {
        this.visibility = View.GONE
    } else{
        this.text = username
        this.visibility = View.VISIBLE
    }
}

fun SharedPreferences.updateCurrentUser(email: String, password: String){
    val pref = edit()
    pref.putString("currentUserEmail", email.trimBothSides())
    pref.putString("currentUserPassword", password)
    pref.apply()
}

fun SharedPreferences.clearCredential(){
    val pref = edit()
    pref.putString("currentUserEmail", "")
    pref.putString("currentUserPassword", "")
    pref.apply()
}

fun SharedPreferences.getCredentials(): Pair<String, String>{
    val email = getString("currentUserEmail", "")
    val password = getString("currentUserPassword","")

    return Pair(email, password)
}

fun String.trimBothSides() = this.trimStart().trimEnd()

fun String.addCheck() = this.plus("✓")

fun Bitmap.toBase64() : String{
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

fun String.toBitmap() : Bitmap {
    val bitmap = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bitmap, 0 , bitmap.size)
}

fun RecyclerView.ViewHolder.setTime(time: TextView, timestamp: Long){
    time.text = TimeUtil.setTimeFromTimeStamp(timestamp)
}

fun RecyclerView.ViewHolder.setMessageCheckIfSeen(time: TextView, isSent: Boolean){
    if(isSent){
        time.text.toString().addCheck()
    }
}

suspend fun Fragment.loadGridview( gridview: GridView, emojiList: ArrayList<String>){
    withContext(Dispatchers.IO) {
        val adapter = EmojiGridViewAdapter(activity?.applicationContext!!, emojiList)
        withContext(Dispatchers.Main) {
            gridview.adapter = adapter
        }
    }
}

fun ProgressBar.start(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.stop(){
    this.visibility = View.GONE
}
