package com.example.tfm.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tfm.data.DataRepository
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.model.Conversation
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.FirebaseUtil.FIREBASE_USER_PATH
import com.example.tfm.util.removeAfter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConversationViewModel : ViewModel(){
    private var roomDatabase: MyRoomDatabase? = null
    private val conversationList: MutableLiveData<MutableList<Conversation>> = MutableLiveData()
    private val conversations = mutableListOf<Conversation>()

    fun getConversations(): LiveData<MutableList<Conversation>>{
        return conversationList
    }

    fun initDownloadUserData(){
        CoroutineScope(Dispatchers.IO).launch{
            FirebaseUtil.loadUsers()
        }
    }

    fun filterList(text: String?){
        //TODO not working
        val list = conversations
            .filter { it.userOneEmail.removeAfter('@').contains(text.toString(), ignoreCase = true) ||
                    it.userTwoEmail.removeAfter('@').contains(text.toString(), ignoreCase = true)}
            .toMutableList()

        conversationList.postValue(list)
    }

    fun initRoomObserver(activity: FragmentActivity){
        updateUser(currentUserEmail)
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity)
        roomDatabase?.conversationDao()?.getUserLiveConversations(currentUserEmail)?.observe(activity, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                conversations.clear()

                it.forEach { conversation ->
                    conversations.add(setUpdatedConversation(conversation))
                }

                conversationList.postValue(conversations)
            }
        })
    }

    private fun updateUser(email: String){
        CoroutineScope(Dispatchers.IO).launch {
            val loginTask = FirebaseFirestore.getInstance().collection(FIREBASE_USER_PATH).document(email).get().await()
            val user = loginTask.toObject(User::class.java)
            DataRepository.user = user
        }
    }

    private fun setUpdatedConversation(conversation: Conversation): Conversation{
        var user: User?
        var newConversation: Conversation?

        if(conversation.userOneEmail == currentUserEmail){
            user = roomDatabase?.getUserByEmail(conversation.userTwoEmail)
            newConversation = conversation.copy(userTwoPhoto = user?.profilePhoto!!, userTwoUsername = user.name)
        }else{
            user = roomDatabase?.getUserByEmail(conversation.userOneEmail)
            newConversation = conversation.copy(userOnePhoto = user?.profilePhoto!!, userOneUsername = user.name)

        }

        return newConversation
    }
}