package com.example.tfm.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.ReceiverMessageViewHolder
import com.example.tfm.viewHolder.SenderImageViewHolder
import com.example.tfm.viewHolder.SenderMessageViewHolder

class ChatAdapter(private val messages : MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        var viewType: Int
        val message = messages[position]

        if(message.sender == Sender.OWN){
            when(message.messageType){
                MessageType.MESSAGE -> {
                    viewType = 0
                }

                MessageType.PHOTO -> {
                    viewType = 2
                }

                else -> viewType = -1
            }
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
                return SenderMessageViewHolder(view)
            }

            1 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.receiver_message_viewholder, parent, false)
                return ReceiverMessageViewHolder(view)
            }

            2 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.sender_image_viewholder, parent, false)
                return SenderImageViewHolder(view)
            }
        }

        // TODO never happen I think
        return ReceiverMessageViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is SenderMessageViewHolder -> {
                holder.text.text = message.body
            }

            is ReceiverMessageViewHolder ->{
                holder.text.text = message.body
            }

            is SenderImageViewHolder ->  {
                //holder.image.setImageBitmap()
            }
        }
    }

    override fun getItemCount() = messages.size
}