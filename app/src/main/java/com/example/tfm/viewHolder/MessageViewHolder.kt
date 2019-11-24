package com.example.tfm.viewHolder

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.data.DataRepository.languagePreferenceCode
import com.example.tfm.enum.LanguageCode
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import org.jetbrains.anko.displayMetrics

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){

    @BindView(R.id.message_layout) lateinit var layout: RelativeLayout
    @BindView(R.id.message_placeholder) lateinit var placeholder: LinearLayout
    @BindView(R.id.message_body) lateinit var body: TextView
    @BindView(R.id.message_time) lateinit var time: TextView

    init {
        ButterKnife.bind(this, view)
    }

    fun initMessageViewHolder(message: Message){
        if(message.senderName == currentUserEmail){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        initLayout(message)

        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.senderName == currentUserEmail, message.isSent)
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
        body.setTextColor(context.getColor(R.color.colorWhite))
        body.gravity = Gravity.END
        time.gravity = Gravity.END
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
        body.setTextColor(context.getColor(R.color.colorPrimaryText))
        body.gravity  = Gravity.START
        time.gravity = Gravity.START
    }

    private fun initLayout(message: Message){
        val code = message.body?.fieldThree?.toInt()

        if(code == languagePreferenceCode || languagePreferenceCode == null){
            body.text = message.body?.fieldOne
        }else if(code != LanguageCode.ENGLISH.code && (languagePreferenceCode == LanguageCode.ENGLISH.code || languagePreferenceCode == null)){
            body.text = message.body?.fieldTwo
        }else{
            val translator = DataRepository.fromEnglishTranslator
            translator?.let {
                var textToTranslate = if(message.body?.fieldThree?.toInt() == LanguageCode.ENGLISH.code){
                    message.body?.fieldOne
                }else{
                    message.body?.fieldTwo
                }

                translator.translate(textToTranslate.toString())
                    .addOnSuccessListener { translatedText ->
                        body.text = translatedText
                    }
            }
        }
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}