package com.example.tfm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tfm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class LocationSenderActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapview: MapView
    var googleMap: GoogleMap? = null
    val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)

        val toolbar: Toolbar =  findViewById(R.id.location_sender_toolbar)
        val toolbarTitle: TextView = findViewById(R.id.location_toolbar_title)

        toolbarTitle.text = getString(R.string.current_location)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var mapViewBundle: Bundle? = null
        savedInstanceState?.let {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

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
        googleMap = gMap
        googleMap?.apply {
            setMinZoomPreference(12F)
            val latlng = LatLng(40.7143528, -74.0059731)
            moveCamera(CameraUpdateFactory.newLatLng(latlng))
        }
    }
}
