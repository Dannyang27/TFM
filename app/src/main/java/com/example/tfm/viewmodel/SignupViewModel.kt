package com.example.tfm.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.util.FirebaseUtil

class SignupViewModel : ViewModel(){

    companion object{
        val isJoinUsSuccessful = MutableLiveData<Boolean>()
    }

    fun getIsJoinSuccessful(): LiveData<Boolean>{
        return isJoinUsSuccessful
    }

    fun joinNewUser(context: Context, username: String, email: String, password: String){
        FirebaseUtil.createNewUser(context, username, email, password)
    }
}