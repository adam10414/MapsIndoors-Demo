package com.example.mapsindoorsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapsindoorsdemo.databinding.ActivityMapsBinding
import com.mapsindoors.coresdk.MapControl
import com.mapsindoors.coresdk.MapsIndoors
import com.mapsindoors.googlemapssdk.MPMapConfig
import com.mapsindoors.googlemapssdk.converters.LatLngBoundsConverter

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapView: View
    private lateinit var mMapControl: MapControl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        MapsIndoors.load(applicationContext, getString(R.string.maps_indoors_key), null)

        mapFragment.view?.let{
            mapView = it
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fun initMapControl(view: View){
            var mapConfig = MPMapConfig.Builder(this, mMap, getString(R.string.google_maps_key), view, true).build()

            // New instance of MapControl
            MapControl.create(mapConfig) {mapControl, miError ->
                if (miError == null){
                    mMapControl = mapControl!!

                    val venue = MapsIndoors.getVenues()?.currentVenue
                    venue?.bounds?.let{
                        runOnUiThread {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBoundsConverter.toLatLngBounds(it), 19))
                        }
                    }
                }
            }
        }

        mapView?.let { view ->
            initMapControl(view)
        }



        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}