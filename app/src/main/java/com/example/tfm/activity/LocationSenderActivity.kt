package com.example.tfm.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.util.AuthUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.toast
import java.util.*

class LocationSenderActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mapview: MapView
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var  fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationAddressTv: TextView
    private lateinit var toolbarTitle: TextView
    private lateinit var address: Address

    private lateinit var currentLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)

        val toolbar: Toolbar =  findViewById(R.id.location_sender_toolbar)
        toolbarTitle = findViewById(R.id.location_toolbar_title)
        val searchView: SearchView = findViewById(R.id.location_searchview)
        val locationSendButton: Button = findViewById(R.id.location_sender_button)
        locationAddressTv = findViewById(R.id.location_sender_address)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val address = Geocoder(this@LocationSenderActivity, Locale.getDefault())
                    .getFromLocationName(query, 1)

                if(address.isNotEmpty()) {
                    moveToLocation(LatLng(address[0].latitude, address[0].longitude))
                }else{
                    toast("Could not find place")
                }

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    searchView.clearFocus()
                }
                return true
            }
        })

        locationSendButton.setOnClickListener {
            ChatActivity.sendMessage(Message(AuthUtil.getAccountEmail(), AuthUtil.receiverEmail, MessageType.LOCATION, address, 1, true, true, true, "EN"))
            finish()
        }

        searchView.queryHint = getString(R.string.search_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapview = findViewById(R.id.location_mapview)
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)

        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if(locationPermission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val latitude = location?.latitude
                val longitude = location?.longitude
                val latlng = LatLng(latitude!!, longitude!!)
                moveToLocation(latlng)
            }
        }
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
        moveToLocation(newPoint)
    }

    private fun moveToLocation(location: LatLng){
        currentLocation = location

        googleMap?.apply {
            clear()
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16F))
            addMarker(MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        address = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
        locationAddressTv.text = address.getAddressLine(0)
        toolbarTitle.text = address.adminArea
    }
}