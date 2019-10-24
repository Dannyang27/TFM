package com.example.tfm.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tfm.data.DataRepository
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase

class UserSearcherViewModel : ViewModel(){

    private var roomDatabase: MyRoomDatabase? = null
    private val users = MutableLiveData<MutableList<User>>()

    fun getUsers(): LiveData<MutableList<User>> {
        return users
    }

    fun searchUsersInCache(text: String?){
        val listFiltered = users.value!!
            .filter {
                it.name.contains(text.toString(), ignoreCase = true)
            }
            .toMutableList()

        users.postValue(listFiltered)
    }

    fun initRoomObserver(activity: FragmentActivity){
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity)
        roomDatabase?.userDao()?.getAll()?.observe(activity, Observer {
            it.remove(DataRepository.user)
            users.postValue(it)
        })
    }
}