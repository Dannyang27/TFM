package com.example.tfm.viewHolder

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.data.DataRepository.conversationPositionClicked
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.model.Conversation
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.setTime
import com.example.tfm.util.showDialog
import com.example.tfm.util.toBitmap
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast

class ConversationViewHolder(view: View) : RecyclerView.ViewHolder(view){

    @BindView(R.id.profile_image) lateinit var image: CircleImageView
    @BindView(R.id.name_chat) lateinit var name: EmojiTextView
    @BindView(R.id.last_message_time_chat) lateinit var lastTime: TextView
    @BindView(R.id.last_message_chat) lateinit var lastMessage: EmojiTextView
    @BindView(R.id.unread_message_number) lateinit var unreadMessages: TextView

    private var id = ""
    private var email = ""
    private var imageBase64 = ""
    private var username = ""

    init {
        ButterKnife.bind(this, view)

        view.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(it.context)
            val language = pref.getString("chatLanguage", "Default")
            if(language == "Default"){
                it.context.toast(it.context.getString(R.string.selectLanguagePreference))
            }else{
                conversationPositionClicked = adapterPosition

                val ctx = it.context
                val intent = Intent(ctx, ChatActivity::class.java)
                intent.putExtra("conversationId", id)
                intent.putExtra("receiverEmail", email)
                intent.putExtra("receiverName", username)
                intent.putExtra("profilePhoto", imageBase64)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val options = ActivityOptions.makeSceneTransitionAnimation(
                    ctx as Activity, image as View, ctx.getString(R.string.image_transition))

                ctx.startActivity(intent, options.toBundle())
            }
        }
    }

    fun bindViewHolder(conversation: Conversation){
        id = conversation.id

        if(conversation.userOneEmail.contains(currentUserEmail)){
            email = conversation.userTwoEmail
            username = conversation.userTwoUsername
            name.text = username
            imageBase64 = conversation.userTwoPhoto
        }else{
            email = conversation.userOneEmail
            username = conversation.userOneUsername
            name.text = username
            imageBase64 = conversation.userOnePhoto
        }

        if(!imageBase64.isNullOrEmpty()){
            Glide.with(itemView.context).load(imageBase64.toBitmap()).into(image)
        }

        lastMessage.text = if(conversation.lastMessage.toString().isNotEmpty()) conversation.lastMessage else image.context.getString(R.string.bethefirst)
        setTime(lastTime, conversation.timestamp)

        CoroutineScope(Dispatchers.IO).launch {
            val unreadMsgs = MyRoomDatabase.INSTANCE?.messageDao()?.getUnreadMessagesFromConversation(id)
            withContext(Dispatchers.Main){
                if(unreadMsgs == 0){
                    unreadMessages.visibility = View.GONE
                }else {
                    unreadMessages.text = unreadMsgs.toString()
                }
            }
        }
    }

    @OnClick(R.id.profile_image)
    fun imageClick(){
        image.showDialog( image.context, imageBase64)
    }
}