package com.example.tfm.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.activity.ChatActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.FirebaseUtil
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.toast
import java.util.*

class LocationViewModel : ViewModel(){
    private val latLng = MutableLiveData<LatLng>()
    private val address = MutableLiveData<Address>()

    fun getLatLng(): LiveData<LatLng> = latLng
    fun getAddress(): LiveData<Address> = address

    fun setLatLng(newLatLng: LatLng){
        latLng.postValue(newLatLng)
    }

    fun setNewLocation(context: Context){
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(latLng.value?.latitude!!, latLng.value?.longitude!!, 1)[0]
        this.address.postValue(address)
    }

    fun searchLocation(context: Context, query: String?){
        val address = Geocoder(context, Locale.getDefault()).getFromLocationName(query, 1)
        if(address.isNotEmpty()) {
            latLng.postValue(LatLng(address[0].latitude, address[0].longitude))
        }else{
            context.toast("No results")
        }
    }

    fun sendLocation(context: Context){
        val timestamp = System.currentTimeMillis()
        val message = Message(
            timestamp,
            ChatActivity.conversationId,
            DataRepository.currentUserEmail,
            ChatActivity.receiverUser,
            MessageType.LOCATION.value,
            MessageContent(address.value?.latitude.toString(), address.value?.longitude.toString(), address.value?.getAddressLine(0).toString()),
            timestamp)

        FirebaseUtil.addMessageFirebase(message)
    }
}