package com.example.tfm.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.util.FirebaseUtil

class LoginViewModel : ViewModel(){

    companion object{
        val isLoading = MutableLiveData<Boolean>()
        val isSuccessful = MutableLiveData<Boolean>()
    }

    fun startLogin(context: Context, user: String, password: String){
        isLoading.postValue(true)
        Log.d("TFM", "User: $user | pass: $password")
        FirebaseUtil.login(context, user, password)
    }

    fun getIsLoading(): LiveData<Boolean>{
        return isLoading
    }

    fun getIsSuccessful(): LiveData<Boolean>{
        return isSuccessful
    }

    fun reset(){
        isSuccessful.postValue(false)
        isLoading.postValue(false)
    }
}