package com.example.businesswatcher.project.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val filePermission: String = Manifest.permission.READ_EXTERNAL_STORAGE
const val locationPermission: String = Manifest.permission.ACCESS_COARSE_LOCATION

fun isFileAccessAllowed(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, filePermission) == PackageManager.PERMISSION_GRANTED

fun askFileAccessPermission(activity: Activity) =
    ActivityCompat.requestPermissions(activity, arrayOf(filePermission), 34)

fun pickPhoto() = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

fun isLocationAllowed(context: Context): Boolean = ContextCompat.checkSelfPermission(
    context, locationPermission
) == PackageManager.PERMISSION_GRANTED

fun askLocationPermission(activity: Activity) =
    ActivityCompat.requestPermissions(activity, arrayOf(locationPermission), 100)