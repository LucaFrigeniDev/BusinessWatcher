package com.example.businesswatcher.project.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_table")
data class Company(
    @PrimaryKey @ColumnInfo(name = "company")
    val id: Long,
    var name: String,
    //var type: Type,
    //var group: Group,
    var adress: String,
    var latitude: Double,
    var longitude: Double,
    var turnover: Int,
    //var customers: List<Customer>,
    var description: String
)