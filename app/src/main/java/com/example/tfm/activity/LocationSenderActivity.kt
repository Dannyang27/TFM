package com.example.tfm.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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

    @BindView(R.id.location_sender_toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.location_toolbar_title) lateinit var toolbarTitle: TextView
    @BindView(R.id.location_mapview) lateinit var mapview: MapView
    @BindView(R.id.location_searchview) lateinit var searcher: SearchView
    @BindView(R.id.location_sender_address) lateinit var currentLocation: TextView
    @BindView(R.id.location_sender_button) lateinit var bSend: Button

    private lateinit var locationViewModel: LocationViewModel
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var  fusedLocationClient: FusedLocationProviderClient
    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_sender)
        ButterKnife.bind(this)

        setSupportActionBar(location_sender_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mapview = findViewById(R.id.location_mapview)

        searcher.queryHint = getString(R.string.search_title)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        locationViewModel.getLatLng().observe(this, androidx.lifecycle.Observer {
            moveToLocation(it)
        })

        locationViewModel.getAddress().observe(this, androidx.lifecycle.Observer {
            address = it
            currentLocation.text = address.getAddressLine(0)
            toolbarTitle.text = address.adminArea
        })

        searcher.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                locationViewModel.searchLocation(applicationContext, query)
                searcher.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    searcher.clearFocus()
                }
                return true
            }
        })

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

        locationViewModel.setNewLocation(applicationContext)
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

    @OnClick(R.id.location_sender_button)
    fun sendLocation(){
        locationViewModel.sendLocation(applicationContext)
        finish()
    }
}