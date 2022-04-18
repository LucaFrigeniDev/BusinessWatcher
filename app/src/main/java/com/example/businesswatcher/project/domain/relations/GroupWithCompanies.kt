package com.example.businesswatcher.project.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.entities.Group

data class GroupWithCompanies(
    @Embedded val group: Group,
    @Relation(parentColumn = "id", entityColumn = "groupId")
    val companies: List<Company>
)

