package com.example.tfm.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.tfm.R

class ReceiverViewHolder( view: View) : RecyclerView.ViewHolder(view){
    val text = view.findViewById<TextView>(R.id.receiverMessage)
}