package com.example.tfm.viewHolder

import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

class AttachmentViewHolder (view: View): RecyclerView.ViewHolder(view){
    @BindView(R.id.pdf_layout) lateinit var layout: RelativeLayout
    @BindView(R.id.pdf_title) lateinit var title: TextView
    @BindView(R.id.pdf_weight) lateinit var weight: TextView
    @BindView(R.id.pdf_time) lateinit var time: TextView

    init {
        ButterKnife.bind(this, view)
        view.setOnClickListener {
            layout.context.toast("Downloading pdf...")
        }
    }

    fun initAttachmentViewHolder(message: Message){
        if(message.senderName == currentUserEmail){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        setTitle("dni_pasaporte", ".pdf")
        setPdfWeightInMB(15.5)
        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.senderName == currentUserEmail, message.isSent)
    }

    private fun setSenderViewHolder(){
        layout.setPadding(getDpValue(60), getDpValue( 10), getDpValue( 15), getDpValue( 10))
        layout.gravity = Gravity.RIGHT
        time.gravity = Gravity.RIGHT
    }

    private fun setReceiverViewHolder(){
        layout.setPadding(getDpValue( 15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.LEFT
        time.gravity = Gravity.LEFT
    }

    private fun setTitle(title: String, extension: String){
        this.title.text = title + extension
    }

    private fun setPdfWeightInMB( weight: Double){
        this.weight.text = "$weight MB"
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}