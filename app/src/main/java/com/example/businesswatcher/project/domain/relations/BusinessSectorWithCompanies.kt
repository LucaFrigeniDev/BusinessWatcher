package com.example.businesswatcher.project.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company

data class BusinessSectorWithCompanies(
    @Embedded val businessSector: BusinessSector,
    @Relation(parentColumn = "id", entityColumn = "businessSectorId")
    val companies: MutableList<Company>
)
