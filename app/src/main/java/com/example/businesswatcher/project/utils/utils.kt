package com.example.businesswatcher.project.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.businesswatcher.R
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Group
import com.example.businesswatcher.project.ui.MapCompanyMarker
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun setPhotoUrl(url: String): String {
    return "https://firebasestorage.googleapis.com/v0/b/businesswatcher.appspot.com/o/$url?alt=media&token=cd8bdd65-d6e6-4403-8b07-5c5d8e8fa91f"
}

fun toast(context: Context, text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun dropDownMenu(
    context: Context,
    groups: List<Group>?,
    sectors: List<BusinessSector>?,
    input: AutoCompleteTextView
) {
    val names: MutableList<String> = mutableListOf()
    if (sectors != null) for (businessSector in sectors) names.add(businessSector.name)
    else if (groups != null) {
        names.add(0, "Independent")
        for (group in groups) names.add(group.name)
    }

    input.setAdapter(ArrayAdapter(context, R.layout.dropdown_item, names))
    input.setDropDownBackgroundResource(R.color.primaryColor)
}

fun dialogBuilder(context: Context, type: String): MaterialAlertDialogBuilder {
    var title = ""
    lateinit var message: String
    lateinit var negativeBtn: String

    when (type) {
        "Sector" -> {
            title = context.getString(R.string.add_sector_dialog_title)
            message = context.getString(R.string.add_sector_dialog_message)
            negativeBtn = context.getString(R.string.add_sector_dialog_negative_btn)
        }
        "Description" -> {
            message = context.getString(R.string.description_dialog_message)
            negativeBtn = context.getString(R.string.add_sector_dialog_negative_btn)
        }
        "Customer" -> {
            message = context.getString(R.string.add_customer_dialog_message)
            negativeBtn = context.getString(R.string.add_sector_dialog_negative_btn)
        }
    }

    val alertDialog = MaterialAlertDialogBuilder(context)
    if (title.isNotBlank()) alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setNegativeButton(negativeBtn) { dialog, _ -> dialog.dismiss() }

    return alertDialog
}

fun getMarkerIcon(root: ViewGroup, color: String, url: String): BitmapDescriptor {
    val markerView = MapCompanyMarker(root, color, url)
    markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
    markerView.isDrawingCacheEnabled = true
    markerView.invalidate()
    markerView.buildDrawingCache(false)
    return BitmapDescriptorFactory.fromBitmap(markerView.drawingCache)
}
