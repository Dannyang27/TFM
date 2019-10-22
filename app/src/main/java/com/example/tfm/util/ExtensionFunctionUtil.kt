package com.example.tfm.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.LanguageDrawable
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

fun SharedPreferences.getLanguage(): String?{
    return getString("chatLanguage", "Default")
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

fun Fragment.vibratePhone(){
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
}

fun ProgressBar.start(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.stop(){
    this.visibility = View.GONE
}

fun Activity.loadImageFromUri(uri: String?): Bitmap{
    val filePath : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
    var file = ""

    uri?.let{
        val url = Uri.parse(it)
        val cursor = contentResolver.query(url, filePath, null, null, null)
        if(cursor!!.moveToFirst()){
            val columnIndex = cursor.getColumnIndex(filePath[0])
            file = cursor.getString(columnIndex)
        }
        cursor.close()
    }

    return BitmapFactory.decodeFile(file)
}

fun Bitmap.createNewBitmap(): Bitmap {
    val proportion: Double = (this.height).toDouble() / this.width
    return Bitmap.createScaledBitmap(this, 500, (500 * proportion).toInt(), false)
}

fun Activity.setBitmapToImageView(placeholder: ImageView, bitmap: Bitmap){
    val imageAspectRatio = bitmap.height / bitmap.width
    Glide.with(this)
        .load(bitmap)
        .override(placeholder.width, placeholder.width * imageAspectRatio)
        .into(placeholder)
}

fun ImageView.rotate(){
    val matrix = Matrix().apply {
        postRotate(-90F)
    }
    val bitmap = (drawable as BitmapDrawable).bitmap
    val bitmapRotated = Bitmap.createBitmap(bitmap, 0,0, bitmap.width, bitmap.height, matrix, true)
    setImageBitmap(bitmapRotated)
}

fun String.isNotCurrentUser() = !isCurrentUser()
fun String.isCurrentUser() = this == DataRepository.currentUserEmail
fun String.isUserLanguagePreference() = toInt() == DataRepository.languagePreferenceCode

fun Long.getConversation(friendId: Long): String{
    if(this < friendId){
        return this.toString().plus(friendId)
    }else{
        return friendId.toString().plus(this)
    }
}

fun String.getDrawable(): Int{
    when(this.toUpperCase()){
        LanguageDrawable.ARABIC.name -> return LanguageDrawable.ARABIC.drawable
        LanguageDrawable.DUTCH.name -> return LanguageDrawable.DUTCH.drawable
        LanguageDrawable.CATALAN.name -> return LanguageDrawable.CATALAN.drawable
        LanguageDrawable.CHINESE.name -> return LanguageDrawable.CHINESE.drawable
        LanguageDrawable.FRENCH.name -> return LanguageDrawable.FRENCH.drawable
        LanguageDrawable.GERMANY.name -> return LanguageDrawable.GERMANY.drawable
        LanguageDrawable.INDIAN.name -> return LanguageDrawable.INDIAN.drawable
        LanguageDrawable.ITALIAN.name -> return LanguageDrawable.ITALIAN.drawable
        LanguageDrawable.JAPANESE.name -> return LanguageDrawable.JAPANESE.drawable
        LanguageDrawable.KOREAN.name -> return LanguageDrawable.KOREAN.drawable
        LanguageDrawable.RUSSIAN.name -> return LanguageDrawable.RUSSIAN.drawable
        LanguageDrawable.SPANISH.name -> return LanguageDrawable.SPANISH.drawable
        else -> { return R.drawable.white_placeholder }
    }
}