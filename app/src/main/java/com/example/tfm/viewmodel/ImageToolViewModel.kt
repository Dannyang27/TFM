package com.example.tfm.viewmodel

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.enum.MediaSource
import com.example.tfm.util.loadImageFromUri

class ImageToolViewModel : ViewModel(){
    private val image = MutableLiveData<Bitmap>()

    fun getImage(): LiveData<Bitmap> = image

    fun initImageBySource(activity: Activity, uri: String, source: MediaSource){
        var bitmap: Bitmap? = null
        when(source){
            MediaSource.GALLERY -> {
                bitmap = activity.loadImageFromUri(uri)
            }
            MediaSource.CAMERA -> {
                bitmap = BitmapFactory.decodeFile(uri)
            }
        }

        image.postValue(bitmap)
    }
}