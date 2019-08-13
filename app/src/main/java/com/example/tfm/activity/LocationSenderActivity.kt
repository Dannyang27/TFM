package com.example.tfm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationSenderActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mapview: MapView
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)

        val toolbar: Toolbar =  findViewById(R.id.location_sender_toolbar)
        val toolbarTitle: TextView = findViewById(R.id.location_toolbar_title)
        val searchView: SearchView = findViewById(R.id.location_searchview)

        toolbarTitle.text = getString(R.string.current_location)
        searchView.queryHint = getString(R.string.search_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mapview = findViewById(R.id.location_mapview)
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)

        var mapViewBundle = outState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        if(mapViewBundle == null){
            mapViewBundle = Bundle()
            outState?.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapview.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapview.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapview.onLowMemory()
    }

    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap?.apply {
            setMinZoomPreference(12F)
            val latlng = LatLng(38.8407800, 0.1057400)
            moveCamera(CameraUpdateFactory.newLatLng(latlng))
            addMarker(MarkerOptions().position(latlng))
            setOnMapLongClickListener(this@LocationSenderActivity)
        }
    }

    override fun onMapLongClick(newPoint: LatLng) {
        //remove all markers
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions()
            .position(newPoint)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
    }
}