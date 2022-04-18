package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentMapBinding
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.viewmodel.MapsViewModel
import com.example.businesswatcher.project.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _bind: FragmentMapBinding? = null
    private val bind get() = _bind!!

    private val viewModel: MapsViewModel by viewModels()

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        displayFragment = "MapsFragment"
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return bind.root
    }

    override fun onResume() {
        super.onResume()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_maps_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled
        map.uiSettings.isMyLocationButtonEnabled

        setMap()
        locationFAB()
        setMarkers()
        filter()
        clearFilter()
    }

    private fun locationFAB() = bind.locationFAB.setOnClickListener {
        when (isLocationAllowed(requireContext())) {
            true -> setMap()
            false -> askLocationPermission(requireActivity())
        }
    }

    private fun setMap() {
        getDeviceLocation(requireActivity())
        if (userLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 8.0F))
        }
    }

    private fun filter() = (activity as MainActivity).filterBtn.setOnClickListener {
        (activity as MainActivity).setFilters()
        (activity as MainActivity).hideBottomSheet()
        setMarkers()
    }

    private fun clearFilter() = (activity as MainActivity).clearBtn.setOnClickListener {
        (activity as MainActivity).clear()
        setMarkers()
    }

    private fun setMarkers() {
        viewModel.getBusinessSectorsWithCompanies().observe(viewLifecycleOwner) {
            map.clear()
            var markerTitle: String
            var markerColor: String

            for (businessSectorCompany in it) {
                for (company in businessSectorCompany.companies) {
                    viewModel.getGroupName(company.groupId, company.name)
                        .observe(viewLifecycleOwner) { title ->
                            markerColor = businessSectorCompany.businessSector.color
                            markerTitle = title

                            map.addMarker(
                                MarkerOptions()
                                    .title(markerTitle)
                                    .position(LatLng(company.latitude, company.longitude))
                                    .snippet(company.city)
                                    .icon(
                                        getMarkerIcon(
                                            bind.root as ViewGroup, markerColor, company.logo
                                        )
                                    )
                            )!!.tag = company.id
                        }
                }
            }
            onMarkerClick()
        }
    }

    private fun onMarkerClick() =
        map.setOnInfoWindowClickListener { marker -> mapToCompanyDetail(marker.tag as Long) }
}