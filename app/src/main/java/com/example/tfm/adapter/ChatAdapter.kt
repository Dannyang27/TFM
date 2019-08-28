package com.example.tfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.AttachmentViewHolder
import com.example.tfm.viewHolder.ImageViewHolder
import com.example.tfm.viewHolder.LocationViewHolder
import com.example.tfm.viewHolder.MessageViewHolder

class ChatAdapter(private val messages : MutableList<Message>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val context = context

    override fun getItemViewType(position: Int) =  when(messages[position].messageType){
        MessageType.MESSAGE -> MessageType.MESSAGE.value
        MessageType.IMAGE, MessageType.GIF -> MessageType.IMAGE.value
        MessageType.LOCATION -> MessageType.LOCATION.value
        MessageType.ATTACHMENT -> MessageType.ATTACHMENT.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?

        return when(MessageType.fromInt(viewType)){
            MessageType.MESSAGE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_message, parent, false)
                return MessageViewHolder(view)
            }

            MessageType.IMAGE, MessageType.GIF ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_image, parent, false)
                return ImageViewHolder(view)
            }

            MessageType.LOCATION -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_location, parent, false)
                return LocationViewHolder(view)
            }

            MessageType.ATTACHMENT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_attachment, parent, false)
                return AttachmentViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is MessageViewHolder -> {
                holder.initMessageViewHolder(message)
            }

            is ImageViewHolder ->  {
                holder.initImageViewHolder(message)
            }

            is LocationViewHolder -> {
                holder.initAndUpdateMap(context, message)
            }

            is AttachmentViewHolder -> {
                holder.initAttachmentViewHolder(message)
            }
        }
    }

    override fun getItemCount() = messages.size
}