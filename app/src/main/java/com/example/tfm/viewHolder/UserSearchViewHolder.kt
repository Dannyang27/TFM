package com.example.tfm.viewHolder

import android.view.View
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import de.hdodenhof.circleimageview.CircleImageView

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view){

    lateinit var email: String
    lateinit var user: String
    lateinit var photoBase64: String

    var id: Long = -1
    val photo: CircleImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)

//    init {
//        view.setOnClickListener {
//            val userId = DataRepository.user?.id?.toLong()
//            val conversationId = userId?.getConversation(id)
//            CoroutineScope(Dispatchers.IO).launch {
//                val firestore = FirebaseFirestore.getInstance()
//                val conversation = firestore.getConversation(conversationId.toString())
//                if(conversation != null){
//                    it.context.launchChatActivity(conversationId.toString(), email, user, photoBase64, false)
//                }else{
//                    firestore.createNewConversation(username.context, DataRepository.currentUserEmail, email)
//                }
//            }
//        }
//    }
}