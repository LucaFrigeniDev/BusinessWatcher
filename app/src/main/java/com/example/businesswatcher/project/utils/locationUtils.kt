package com.example.businesswatcher.project.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.businesswatcher.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.io.IOException
import java.util.*

var userLocation: LatLng? = null

@SuppressLint("StaticFieldLeak")
lateinit var fusedLocationClient: FusedLocationProviderClient

@SuppressLint("MissingPermission")
fun getDeviceLocation(activity: Activity) =
    fusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
        if (task.isSuccessful && task.result != null) {
            userLocation = LatLng(task.result.latitude, task.result.longitude)
        } else toast(activity, activity.getString(R.string.error_geolocation))
    }

fun geoLocate(locationName: String?, context: Context): Address? {
    var address: Address? = null
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addressList: List<Address> = geocoder.getFromLocationName(locationName, 1)
        if (addressList.isNotEmpty()) address = addressList[0]
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return address
}

fun getDistance(lat: Double, lng: Double): Int =
    SphericalUtil.computeDistanceBetween(userLocation, LatLng(lat, lng)).toInt()