package com.example.businesswatcher.project.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_table")
data class Group(
    @PrimaryKey @ColumnInfo(name = "group")
    val id: Long,
    var name: String,
    var logo: String,
    var turnover: Int,
    var description: String,
)