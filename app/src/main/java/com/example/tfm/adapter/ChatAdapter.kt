package com.example.tfm.adapter

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.ReceiverImageViewHolder
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

                MessageType.PHOTO, MessageType.GIF -> {
                    viewType = 2
                }

                else -> viewType = -1
            }
        }else{
            when(message.messageType){
                MessageType.MESSAGE -> {
                    viewType = 1
                }

                MessageType.PHOTO -> {
                    viewType = 3
                }

                else -> viewType = -1
            }
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null

        when(viewType){
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_message, parent, false)
                return SenderMessageViewHolder(view)
            }

            1 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receiver_message, parent, false)
                return ReceiverMessageViewHolder(view)
            }

            2 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_image, parent, false)
                return SenderImageViewHolder(view)
            }

            3 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receiver_image, parent, false)
                return ReceiverImageViewHolder(view)
            }
        }

        // TODO never happen I think
        return ReceiverMessageViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is SenderMessageViewHolder -> {
                holder.text.text = message.body as String
                holder.time.text = "00:00"
            }

            is ReceiverMessageViewHolder ->{
                holder.text.text = message.body as String
                holder.time.text = "01:21"
            }

            is SenderImageViewHolder ->  {
                val mediaContent = message.body as MediaContent
                if(message.messageType == MessageType.PHOTO){
                    holder.image.setImageBitmap(mediaContent.content as Bitmap)
                }else{
                    Glide.with(holder.image.context)
                        .asGif()
                        .centerCrop()
                        .load(mediaContent.content as String)
                        .into(holder.image)
                }

                if(mediaContent.caption.isNotEmpty()){
                    holder.caption.text = mediaContent.caption
                    holder.caption.visibility = View.VISIBLE
                }
            }

            is ReceiverImageViewHolder -> {
                // TODO
            }
        }
    }

    override fun getItemCount() = messages.size
}