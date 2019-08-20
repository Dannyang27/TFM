package com.example.tfm.adapter

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.enum.ViewTypeEnum
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.*
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.toast

class ChatAdapter(private val messages : MutableList<Message>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val context = context

    override fun getItemViewType(position: Int) =  when(messages[position].messageType){
        MessageType.MESSAGE -> MessageType.MESSAGE.value
        MessageType.MEDIA -> MessageType.MEDIA.value
        MessageType.LOCATION -> MessageType.LOCATION.value
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?

        return when(MessageType.fromInt(viewType)){
            MessageType.MESSAGE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_message, parent, false)
                return SenderMessageViewHolder(view)
            }

            MessageType.MEDIA ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_image, parent, false)
                return SenderImageViewHolder(view)
            }

            MessageType.LOCATION -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_location, parent, false)
                return LocationViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is SenderMessageViewHolder -> {
                holder.text.text = message.body as String
                holder.time.text = "00:00"
            }

//            is ReceiverMessageViewHolder ->{
//                holder.text.text = message.body as String
//                holder.time.text = "01:21"
//            }

            is SenderImageViewHolder ->  {
                val mediaContent = message.body as MediaContent
                holder.image.setImageBitmap(mediaContent.content as Bitmap)
                holder.image.setOnClickListener {
                    holder.image.context.toast("Image pressed, expanding...")
                }

//                }else{
//                    Glide.with(holder.image.context)
//                        .asGif()
//                        .centerCrop()
//                        .load(mediaContent.content as String)
//                        .into(holder.image)
//                }

                if(mediaContent.caption.isNotEmpty()){
                    holder.caption.text = mediaContent.caption
                    holder.caption.visibility = View.VISIBLE
                }
            }

//            is ReceiverImageViewHolder -> {
//                val mediaContent = message.body as MediaContent
//                if(message.messageType == MessageType.PHOTO){
//                    holder.image.setImageBitmap(mediaContent.content as Bitmap)
//                    holder.image.setOnClickListener {
//                        holder.image.context.toast("Image pressed, expanding...")
//                    }
//                }else{
//                    Glide.with(holder.image.context)
//                        .asGif()
//                        .centerCrop()
//                        .load(mediaContent.content as String)
//                        .into(holder.image)
//                }
//
//                if(mediaContent.caption.isNotEmpty()){
//                    holder.caption.text = mediaContent.caption
//                    holder.caption.visibility = View.VISIBLE
//                }
//            }

            is LocationViewHolder -> {
                val address = message.body as Address
                holder.place.text = address.getAddressLine(0)
                holder.time.text = "11:11"
                holder.initAndUpdateMap(context, LatLng(address.latitude, address.longitude))
            }
        }
    }

    override fun getItemCount() = messages.size
}