package com.example.tfm.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.google.android.gms.maps.MapView

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mapView: MapView = view.findViewById(R.id.mapview_location_viewholder)
    val place: TextView = view.findViewById(R.id.place_location_viewholder)
    val time: TextView = view.findViewById(R.id.time_location_viewholder)
}