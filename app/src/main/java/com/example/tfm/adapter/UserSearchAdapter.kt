package com.example.tfm.adapter

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.diffUtil.UserDiffCallback
import com.example.tfm.model.Conversation
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.*
import com.example.tfm.viewHolder.UserSearchViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class UserSearchAdapter (private var users: MutableList<User>): RecyclerView.Adapter<UserSearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        val user = users[position]
        holder.email = user.email
        holder.user = user.name
        holder.photoBase64 = user.profilePhoto

        holder.id = user.id.toLong()
        holder.username.text = user.name
        holder.status.text = user.status
        holder.photo

        try{
            Glide.with(holder.itemView.context).load(user.profilePhoto.toBitmap()).into(holder.photo)
        }catch (e: Exception){
            Log.d("TFM", "Image null or empty")
        }

        holder.photo.setOnClickListener {
            holder.photo.showDialog(it.context, user.profilePhoto)
        }

        holder.itemView.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(it.context)
            val hasLanguagePref = pref.getString("chatLanguage", "Default") != "Default"

            if(hasLanguagePref){
                createConversationIfNone(it.context, holder)
            }else{
                it.context.toast(it.context.getString(R.string.selectLanguagePreference))
            }
        }
    }

    override fun getItemCount() = users.size

    fun updateList( newUsers : MutableList<User>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(UserDiffCallback(users, newUsers))
        users.clear()
        users.addAll(newUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun createConversationIfNone(context: Context, holder: UserSearchViewHolder){
        val user = DataRepository.user?.id?.toLong()
        val conversationId = user?.getConversation(holder.id)

        CoroutineScope(Dispatchers.IO).launch {
            val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
            val conversation = roomDatabase?.conversationDao()?.getById(conversationId.toString())

            if(conversation != null){
                context.launchChatActivity(conversationId.toString(), holder.email, holder.user, holder.photoBase64, false )
            }else{
                val myself = DataRepository.user
                val friend = roomDatabase?.getUserByEmail(holder.email)
                var userOneHash = myself?.id?.toLong()!!
                var userTwoHash = friend?.id?.toLong()!!

                if(userOneHash > userTwoHash){
                    val tmp = userOneHash
                    userOneHash = userTwoHash
                    userTwoHash = tmp
                }

                val hashcode = userOneHash.toString().plus(userTwoHash.toString())
                val conversation = Conversation(hashcode, myself.email, myself.name, myself.profilePhoto, friend.email, friend.name,friend.profilePhoto,
                    mutableListOf(), "",System.currentTimeMillis(), mutableListOf(), true )

                roomDatabase.addConversation(conversation)
                FirebaseFirestore.getInstance().addConversation(context, conversation)
            }
        }
    }
}