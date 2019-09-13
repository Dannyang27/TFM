package com.example.tfm.viewHolder

import android.preference.PreferenceManager
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Message
import com.example.tfm.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.displayMetrics

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view), CoroutineScope{
    override val coroutineContext = Dispatchers.Default + Job()

    private var isOriginalText = false

    private val username: EmojiTextView = view.findViewById(R.id.message_username)
    private val layout : RelativeLayout = view.findViewById(R.id.message_layout)
    private val placeholder: LinearLayout = view.findViewById(R.id.message_placeholder)
    private val userPhoto: ImageView = view.findViewById(R.id.message_image)
    private val body: EmojiTextView = view.findViewById(R.id.message_body)
    private val time: TextView = view.findViewById(R.id.message_time)

    fun initMessageViewHolder(message: Message){
        if(message.senderName == DataRepository.currentUserEmail){
            setSenderViewHolder()
            initLayout(message)
        }else{
            setReceiverViewHolder()
            initLayout(message)
            username.showUsernameIfGroup(true, message.senderName)
        }

        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.isSent)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(getDpValue(40), 0, 0, 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.END
        placeholder.background = context.getDrawable(R.drawable.sender_message)
        layout.setPadding(0,getDpValue(10),getDpValue(15), getDpValue(10))
        userPhoto.visibility = View.GONE
        body.setTextColor(context.getColor(R.color.colorWhite))
        body.gravity = Gravity.END
        time.gravity = Gravity.END
        username.visibility = View.GONE
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(0, 0, getDpValue(40), 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.START
        placeholder.background = context.getDrawable(R.drawable.receiver_message)
        layout.setPadding(getDpValue(15),getDpValue(10), 0, getDpValue(10))
        userPhoto.visibility = View.VISIBLE
        body.setTextColor(context.getColor(R.color.colorPrimaryText))
        body.gravity  = Gravity.START
        time.gravity = Gravity.START
    }

    private fun initLayout(message: Message){
        val pref = PreferenceManager.getDefaultSharedPreferences(layout.context).getLanguage()
        if( pref == "Default" || message.body?.fieldThree?.isUserLanguagePreference()!!){
            this.body.text = message.body?.fieldOne
        }else{
            this.body.text = message.body?.fieldTwo

            itemView.setOnClickListener {
                if(isOriginalText){
                    this.body.text = message.body?.fieldTwo
                }else{
                    this.body.text = message.body?.fieldOne
                }
                isOriginalText = !isOriginalText
            }
        }
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}