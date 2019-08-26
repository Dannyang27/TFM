package com.example.tfm.viewHolder

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.LogUtil
import com.example.tfm.util.checkIfConversationExists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view), CoroutineScope{
    private val job = Job()
    override val coroutineContext = job + Main

    lateinit var email: String
    val photo: ImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)

    init {
        view.setOnClickListener {
            launch {
                var exists = false
                val context = username.context
                val job = async{
                    exists = true
                    FirebaseUtil.database.checkIfConversationExists(email)
                }

                job.await()

                //Log.d(LogUtil.TAG, "Coroutine: $exists")

            }

        }
    }

//            context.toast("Exist: $exist")

//            val intent = Intent(context, ChatActivity::class.java)
//            context.startActivity(intent)

}