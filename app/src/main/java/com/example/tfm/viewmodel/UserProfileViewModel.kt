package com.example.tfm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.data.DataRepository
import com.example.tfm.model.User

class UserProfileViewModel : ViewModel(){
    private val user = MutableLiveData<User>()

    fun initProfile(){
        user.postValue(DataRepository.user)
    }
    fun getUser(): LiveData<User> = user

    fun updateUser(user: User){
        this.user.postValue(user)
    }
}