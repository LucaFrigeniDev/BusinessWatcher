package com.example.businesswatcher.project.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.businesswatcher.project.domain.other.Customer

@Entity(tableName = "company_table")
data class Company(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    val businessSectorId: Long,
    var groupId: Long?,
    val city: String,
    val postalCode: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    var turnover: Int,
    val customers: List<Customer>,
    var description: String,
    var logo: String
)