package com.example.businesswatcher.project.ui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat.getColor
import com.example.businesswatcher.R

class MapCompanyMarker(root: ViewGroup, color: String, url: String) : FrameLayout(root.context) {

    private var background: ImageView
    private var logo: ImageView

    init {
        View.inflate(context, R.layout.marker_maps, this)
        background = findViewById(R.id.background)
        logo = findViewById(R.id.marker_logo)
        //Textview

        when (color) {
            context.getString(R.string.color_1) ->
                background.setColorFilter(getColor(context, R.color.color_sector_1))
            context.getString(R.string.color_2) ->
                background.setColorFilter(getColor(context, R.color.color_sector_2))
            context.getString(R.string.color_3) ->
                background.setColorFilter(getColor(context, R.color.color_sector_3))
            context.getString(R.string.color_4) ->
                background.setColorFilter(getColor(context, R.color.color_sector_4))
            context.getString(R.string.color_5) ->
                background.setColorFilter(getColor(context, R.color.color_sector_5))
        }

        //Glide.with(context).load(url).circleCrop().into(logo)

        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }
}