package com.example.tfm.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.tfm.R
import com.example.tfm.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_location_sender.*

class LocationSenderActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var mapview: MapView
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var  fusedLocationClient: FusedLocationProviderClient
    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)

        setSupportActionBar(location_sender_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mapview = findViewById(R.id.location_mapview)

        location_searchview.queryHint = getString(R.string.search_title)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        locationViewModel.getLatLng().observe(this, androidx.lifecycle.Observer {
            moveToLocation(it)
        })

        locationViewModel.getAddress().observe(this, androidx.lifecycle.Observer {
            address = it
            location_sender_address.text = address.getAddressLine(0)
            location_toolbar_title.text = address.adminArea
        })

        location_searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                locationViewModel.searchLocation(this@LocationSenderActivity, query)
                location_searchview.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    location_searchview.clearFocus()
                }
                return true
            }
        })

        location_sender_button.setOnClickListener {
            locationViewModel.sendLocation(this)
            finish()
        }

        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if(locationPermission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val latlng = LatLng(location?.latitude!!, location.longitude)
                locationViewModel.setLatLng(latlng)
            }
        }
    }

    private fun moveToLocation(location: LatLng){
        googleMap?.apply {
            clear()
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16F))
            addMarker(MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        }

        locationViewModel.setNewLocation(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
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
            setOnMapLongClickListener(this@LocationSenderActivity)
        }
    }

    override fun onMapLongClick(newPoint: LatLng) {
        locationViewModel.setLatLng(newPoint)
    }
}