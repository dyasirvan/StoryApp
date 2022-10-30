package com.story.app.ui.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.story.app.R
import com.story.app.databinding.ActivityMapsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModel()

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        initData()
    }

    private fun initData() {
        viewModel.getStories().observe(this){ stories ->
            stories.forEach { story ->
                val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                mMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                )
                boundsBuilder.include(latLng)
            }

            val latLng = LatLng(stories[0].lat ?: 0.0, stories[0].lon ?: 0.0)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            )
        }
    }

}
