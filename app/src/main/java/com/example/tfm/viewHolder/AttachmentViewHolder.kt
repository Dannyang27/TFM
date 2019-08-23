package com.example.tfm.viewHolder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.model.Message
import com.example.tfm.util.AuthUtil
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

class AttachmentViewHolder (view: View): RecyclerView.ViewHolder(view){
    private val layout: RelativeLayout = view.findViewById(R.id.pdf_layout)
    private val userPhoto: ImageView = view.findViewById(R.id.pdf_image)
    private val title: TextView = view.findViewById(R.id.pdf_title)
    private val weight: TextView = view.findViewById(R.id.pdf_weight)
    private val time: TextView = view.findViewById(R.id.pdf_time)

    init {
        view.setOnClickListener {
            layout.context.toast("Downloading pdf...")
        }
    }

    fun initAttachmentViewHolder(message: Message){
        if(message.senderName == AuthUtil.getAccountEmail()){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        setTitle("dni_pasaporte", ".pdf")
        setPdfWeightInMB(15.5)
    }

    private fun setSenderViewHolder(){
        layout.setPadding(getDpValue(60), getDpValue( 10), getDpValue( 15), getDpValue( 10))
        layout.gravity = Gravity.RIGHT
        userPhoto.visibility = View.GONE
        time.gravity = Gravity.RIGHT
    }

    private fun setReceiverViewHolder(){
        layout.setPadding(getDpValue( 15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.LEFT
        userPhoto.visibility = View.VISIBLE
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