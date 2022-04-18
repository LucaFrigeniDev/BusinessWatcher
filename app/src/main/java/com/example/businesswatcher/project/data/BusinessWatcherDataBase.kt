package com.example.businesswatcher.project.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.businesswatcher.project.data.dao.BusinessSectorDAO
import com.example.businesswatcher.project.data.dao.CompanyDAO
import com.example.businesswatcher.project.data.dao.GroupDAO
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.entities.Group

@Database(
    entities = [BusinessSector::class, Group::class, Company::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class BusinessWatcherDataBase : RoomDatabase() {

    abstract fun businessSectorDao(): BusinessSectorDAO
    abstract fun groupDao(): GroupDAO
    abstract fun companyDao(): CompanyDAO
}