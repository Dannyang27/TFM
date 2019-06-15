package com.example.tfm.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.R
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.ReceiverViewHolder
import com.example.tfm.viewHolder.SenderViewHolder

class ChatAdapter(private val messages : MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        var viewType: Int
        val message = messages[position]

        if(message.sender == Sender.OWN){
            viewType = 0
        }else{
            viewType = 1
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null

        when(viewType){
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.sender_message_viewholder, parent, false)
                return SenderViewHolder(view)
            }

            1 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.receiver_message_viewholder, parent, false)
                return ReceiverViewHolder(view)
            }
        }

        // TODO never happen I think
        return ReceiverViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is SenderViewHolder -> {
                holder.text.text = message.body
            }

            is ReceiverViewHolder ->{
                holder.text.text = message.body
            }
        }
    }

    override fun getItemCount() = messages.size
}