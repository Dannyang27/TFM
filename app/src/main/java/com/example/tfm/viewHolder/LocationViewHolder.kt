package com.example.tfm.viewHolder

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Location
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.displayMetrics

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

    @BindView(R.id.location_layout) lateinit var locationLayout: RelativeLayout
    @BindView(R.id.mapview_location_viewholder) lateinit var mapView: MapView
    @BindView(R.id.place_location_viewholder) lateinit var place: TextView
    @BindView(R.id.time_location_viewholder) lateinit var time: TextView

    private lateinit var context: Context
    private lateinit var googleMap: GoogleMap
    private var latLng: LatLng? = null

    init {
        ButterKnife.bind(this, view)
    }

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

        val content = message.body
        val address = Location(content?.fieldOne.toString().toDouble(),
                               content?.fieldTwo.toString().toDouble(),
                               content?.fieldThree.toString())

        this.latLng = LatLng(address.latitude, address.longitude)

        mapView.onCreate(null)
        mapView.getMapAsync(this)

        if(message.senderName == DataRepository.currentUserEmail){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        setAddress(address.addressLine)
        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.senderName == DataRepository.currentUserEmail, message.isSent)
    }

    private fun setSenderViewHolder(){
        locationLayout.gravity = Gravity.END
        locationLayout.setPadding(0,0,getDpValue(15),0)
        time.gravity = Gravity.END
    }

    private fun setReceiverViewHolder(){
        locationLayout.gravity = Gravity.START
        locationLayout.setPadding(getDpValue(15),0,0,0)
        time.gravity = Gravity.START
    }

    private fun setAddress(addressLine: String){
        this.place.text = addressLine
    }

    private fun getDpValue( dpValue: Int): Int = (dpValue * context.displayMetrics.density).toInt()
}