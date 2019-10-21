package com.example.tfm.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.util.FirebaseUtil
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
import org.jetbrains.anko.toast
import java.util.*

class LocationSenderActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mapview: MapView
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var  fusedLocationClient: FusedLocationProviderClient
    private lateinit var address: Address

    private lateinit var currentLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)

        setSupportActionBar(location_sender_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        location_searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val address = Geocoder(this@LocationSenderActivity, Locale.getDefault())
                    .getFromLocationName(query, 1)

                if(address.isNotEmpty()) {
                    moveToLocation(LatLng(address[0].latitude, address[0].longitude))
                }else{
                    toast("Could not find place")
                }

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
            val timestamp = System.currentTimeMillis()
            val message = Message(timestamp, ChatActivity.conversationId, DataRepository.currentUserEmail, ChatActivity.receiverUser, MessageType.LOCATION.value,
                MessageContent(address.latitude.toString(), address.longitude.toString(), address.getAddressLine(0)), timestamp)

            FirebaseUtil.addMessageLocal(message)
            FirebaseUtil.addMessageFirebase(this, message)

            finish()
        }

        location_searchview.queryHint = getString(R.string.search_title)

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
        location_sender_address.text = address.getAddressLine(0)
        location_toolbar_title.text = address.adminArea
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}