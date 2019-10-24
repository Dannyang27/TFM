package com.example.tfm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil

class UserSearcherViewModel : ViewModel(){

    companion object{
        val allUserList = mutableListOf<User>()
    }
    private val users = MutableLiveData<MutableList<User>>()

    fun getUsers(): LiveData<MutableList<User>> {
        return users
    }

    fun searchUsers(text: String?){
        if(allUserList.isEmpty()){
            FirebaseUtil.loadAllUsers(users, text)
        }else{
            users.postValue(allUserList)
        }
    }

    fun searchUsersInCache(text: String?){
        val listFiltered = allUserList
            .filter {
            it.name.contains(text.toString(), ignoreCase = true)}
            .toMutableList()

        users.postValue(listFiltered)
    }
}