package com.example.tfm.viewHolder

import android.content.Context
import android.location.Address
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import com.example.tfm.util.TimeUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.displayMetrics

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
    lateinit var context: Context
    private lateinit var googleMap: GoogleMap
    private var latLng: LatLng? = null

    private val locationLayout: RelativeLayout = view.findViewById(R.id.location_layout)
    private val userPhoto: ImageView = view.findViewById(R.id.location_image)
    private val mapView: MapView = view.findViewById(R.id.mapview_location_viewholder)
    private val place: TextView = view.findViewById(R.id.place_location_viewholder)
    private val time: TextView = view.findViewById(R.id.time_location_viewholder)

    override fun onMapReady(gMap: GoogleMap) {
        MapsInitializer.initialize(context)
        this.googleMap = gMap.apply {
            clear()
            animateCamera(CameraUpdateFactory.newLatLng(latLng))
            addMarker(
                MarkerOptions()
                    .position(latLng!!)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        }
    }

    fun initAndUpdateMap(ctx: Context, message: Message){
        this.context = ctx

        val address = message.body as Address
        this.latLng = LatLng(address.latitude, address.longitude)

        mapView.onCreate(null)
        mapView.getMapAsync(this)

        if(message.sender == Sender.OWN){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        setAddress(address.getAddressLine(0))
        setTime(message.timestamp)
    }

    private fun setSenderViewHolder(){
        locationLayout.gravity = Gravity.RIGHT
        locationLayout.setPadding(0,0,getDpValue(15),0)
        userPhoto.visibility = View.GONE
        time.gravity = Gravity.RIGHT
    }

    private fun setReceiverViewHolder(){
        locationLayout.gravity = Gravity.LEFT
        locationLayout.setPadding(getDpValue(15),0,0,0)
        userPhoto.visibility = View.VISIBLE
        time.gravity = Gravity.LEFT
    }

    private fun setAddress(addressLine: String){
        this.place.text = addressLine
    }

    private fun setTime(time: Long){
        this.time.text = TimeUtil.setTimeFromTimeStamp(time)
    }

    private fun getDpValue( dpValue: Int): Int = (dpValue * context.displayMetrics.density).toInt()
}