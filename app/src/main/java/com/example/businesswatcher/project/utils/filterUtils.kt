package com.example.businesswatcher.project.utils

import android.content.Context
import android.view.View
import com.example.businesswatcher.R
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Group
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

val businessSectorFilter: MutableList<String> = mutableListOf()
val groupFilter: MutableList<String> = mutableListOf()
var companyFilter: String = ""

fun setChipGroup(
    chipGroup: ChipGroup,
    businessSectors: List<BusinessSector>?,
    groups: List<Group>?,
    context: Context
): ChipGroup {
    chipGroup.removeAllViews()
    if (businessSectors != null) {
        businessSectors.forEach { businessSector ->
            setChip(chipGroup, businessSector.name, businessSector.id.toInt(), context)
        }
    } else if (groups != null) {
        setChip(chipGroup, "Independent", 0, context)
        groups.forEach { group -> setChip(chipGroup, group.name, group.id.toInt(), context) }
    }
    return chipGroup
}

private fun setChip(chipGroup: ChipGroup, name: String, objectId: Int, context: Context): Chip {
    val chip = Chip(context)
    chip.apply {
        text = name
        id = objectId
        isCheckable = true

        chipGroup.addView(chip as View)

        chipStrokeColor = context.getColorStateList(R.color.primaryDarkColor)
        chipBackgroundColor = context.getColorStateList(R.color.primaryDarkColor)
        setTextColor(context.getColorStateList(R.color.primaryColor))

        setOnClickListener {
            if (chip.isChecked) {
                chipStrokeColor = context.getColorStateList(R.color.secondaryColor)
                chipBackgroundColor = context.getColorStateList(R.color.secondaryColor)
                setTextColor(context.getColorStateList(R.color.primaryColor))
            } else {
                chipStrokeColor = context.getColorStateList(R.color.primaryDarkColor)
                chipBackgroundColor = context.getColorStateList(R.color.primaryDarkColor)
                setTextColor(context.getColorStateList(R.color.primaryColor))
            }
        }
    }
    return chip
}