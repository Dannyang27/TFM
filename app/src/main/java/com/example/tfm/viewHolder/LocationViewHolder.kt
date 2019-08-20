package com.example.tfm.viewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
    lateinit var context: Context
    lateinit var googleMap: GoogleMap
    private var latLng: LatLng? = null

    private val mapView: MapView = view.findViewById(R.id.mapview_location_viewholder)
    val place: TextView = view.findViewById(R.id.place_location_viewholder)
    val time: TextView = view.findViewById(R.id.time_location_viewholder)

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

    fun initAndUpdateMap(ctx: Context, latLng: LatLng?){
        this.context = ctx
        this.latLng = latLng
        mapView.onCreate(null)
        mapView.getMapAsync(this)
    }
}