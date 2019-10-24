package com.example.tfm.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Conversation
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.FirebaseUtil

class ConversationViewModel : ViewModel(){
    private var roomDatabase: MyRoomDatabase? = null
    private val conversationList: MutableLiveData<MutableList<Conversation>> = MutableLiveData()

    fun getConversations(): LiveData<MutableList<Conversation>>{
        return conversationList
    }

    fun initConversations(context: Context){
        val user = DataRepository.user
        FirebaseUtil.loadUserConversation(context, user?.id.toString())
//        CoroutineScope(Dispatchers.IO).launch {
//            val list = FirebaseFirestore.getInstance().loadUserConversation(user?.id.toString())
//            conversationList.postValue(list)
//        }
    }

    fun initRoomObserver(activity: FragmentActivity){
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity)
        roomDatabase?.conversationDao()?.getUserConversations(DataRepository.currentUserEmail)?.observe(activity, Observer {
            conversationList.postValue(it)
        })
    }
}