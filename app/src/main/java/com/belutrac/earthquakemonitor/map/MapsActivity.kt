package com.belutrac.earthquakemonitor.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.belutrac.earthquakemonitor.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var earthquake: ArrayList<Earthquake>

    companion object{
        const val EARTHQUAKE_LIST_KEY = "earthquake"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        earthquake = intent?.getParcelableArrayListExtra<Earthquake>(EARTHQUAKE_LIST_KEY) as ArrayList<Earthquake>
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        for (eq in earthquake){
            val place = LatLng(eq.latitude,eq.longitude)
            mMap.addMarker(MarkerOptions()
                .position(place)
                .title(eq.place).snippet("Magnitude: ${eq.magnitude} Richter"))
        }

    }
}