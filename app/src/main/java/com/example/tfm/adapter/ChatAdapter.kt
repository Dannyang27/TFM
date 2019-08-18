package com.example.tfm.adapter

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.enum.ViewTypeEnum
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.util.LogUtil
import com.example.tfm.viewHolder.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.toast

class ChatAdapter(private val messages : MutableList<Message>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnMapReadyCallback{

    private val context = context
    private var googleMap: GoogleMap? = null
    override fun getItemViewType(position: Int): Int {
        var viewType: Int
        val message = messages[position]

        when(message.sender){
            Sender.OWN ->{
                viewType = when(message.messageType){
                    MessageType.MESSAGE -> 0
                    MessageType.PHOTO, MessageType.GIF -> 2
                    MessageType.LOCATION -> 4
                    else ->  -1
                }
            }
            Sender.OTHER -> {
                viewType = when(message.messageType){
                    MessageType.MESSAGE -> 1
                    MessageType.PHOTO, MessageType.GIF -> 3
                    else ->  -1
                }
            }
        }

        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?

        return when(ViewTypeEnum.fromInt(viewType)){
            ViewTypeEnum.OWN_MESSAGE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_message, parent, false)
                return SenderMessageViewHolder(view)
            }

            ViewTypeEnum.OTHER_MESSAGE ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receiver_message, parent, false)
                return ReceiverMessageViewHolder(view)
            }

            ViewTypeEnum.OWN_MEDIA -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender_image, parent, false)
                return SenderImageViewHolder(view)
            }

            ViewTypeEnum.OTHER_MEDIA -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receiver_image, parent, false)
                return ReceiverImageViewHolder(view)
            }

            ViewTypeEnum.LOCATION -> {
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

            is ReceiverMessageViewHolder ->{
                holder.text.text = message.body as String
                holder.time.text = "01:21"
            }

            is SenderImageViewHolder ->  {
                val mediaContent = message.body as MediaContent
                if(message.messageType == MessageType.PHOTO){
                    holder.image.setImageBitmap(mediaContent.content as Bitmap)
                    holder.image.setOnClickListener {
                        holder.image.context.toast("Image pressed, expanding...")
                    }
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
                val mediaContent = message.body as MediaContent
                if(message.messageType == MessageType.PHOTO){
                    holder.image.setImageBitmap(mediaContent.content as Bitmap)
                    holder.image.setOnClickListener {
                        holder.image.context.toast("Image pressed, expanding...")
                    }
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

            is LocationViewHolder -> {
                val address = message.body as Address
                Log.d(LogUtil.TAG, "Lat: ${address.latitude} | Lng: ${address.longitude}")
                Log.d(LogUtil.TAG, "Address Line: ${address.getAddressLine(0)}")

                holder.place.text = address.getAddressLine(0)
                holder.time.text = "11:11"

                holder.mapView.onCreate(null)
                holder.mapView.getMapAsync(this)


                googleMap?.apply {
                    Log.d(LogUtil.TAG, "Running...")
                    val latLng = LatLng(address.latitude, address.longitude)

//                    animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
//                    addMarker(MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                }

            }
        }
    }

    override fun getItemCount() = messages.size

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        MapsInitializer.initialize(context)
    }
}