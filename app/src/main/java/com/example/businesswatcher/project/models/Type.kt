package com.example.businesswatcher.project.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_table")
data class Type(
    @PrimaryKey @ColumnInfo(name = "type")
    var name: String,
    var color: String,
    var marketTurnover: Int
)