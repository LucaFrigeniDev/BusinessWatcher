package com.example.businesswatcher.project.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.businesswatcher.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var locationPermissionGranted: Boolean = false

    private val DEFAULT_ZOOM = 8
    private val PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setLocationFAB(view)
    }

    override fun onResume() {
        super.onResume()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_maps_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        !map.uiSettings.isMapToolbarEnabled
        !map.uiSettings.isMyLocationButtonEnabled

        getDeviceLocation()
    }

    fun setViewModel() {}

    fun setLocationFAB(view: View) {
        view.findViewById<FloatingActionButton>(R.id.location_button).setOnClickListener {
            if (isLocationAllowed()) getDeviceLocation()
            else askLocationPermission()
        }
    }


    // BUG if you deny location access
    fun getDeviceLocation() {
        if (isLocationAllowed()) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(task.result.latitude, task.result.longitude),
                            DEFAULT_ZOOM.toFloat()
                        )
                    )
                } else Toast.makeText(this.context, "aa", Toast.LENGTH_SHORT).show()
            }
        } else askLocationPermission()
    }

    fun isLocationAllowed(): Boolean =
        ContextCompat.checkSelfPermission(
            this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    fun askLocationPermission() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
        )
    }
}