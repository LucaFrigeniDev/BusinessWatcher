package com.example.businesswatcher.project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.businesswatcher.project.database.dao.CompanyDAO
import com.example.businesswatcher.project.database.dao.GroupDAO
import com.example.businesswatcher.project.database.dao.TypeDAO
import com.example.businesswatcher.project.models.Company
import com.example.businesswatcher.project.models.Group
import com.example.businesswatcher.project.models.Type

@Database(
    entities = arrayOf(Type::class, Group::class, Company::class),
    version = 1,
    exportSchema = false
)
abstract class BusinessWatcherDataBase : RoomDatabase() {

    abstract fun typeDao(): TypeDAO
    abstract fun groupDao(): GroupDAO
    abstract fun companyDao(): CompanyDAO

    companion object {

        @Volatile
        private var INSTANCE: BusinessWatcherDataBase? = null

        fun getDatabase(context: Context): BusinessWatcherDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusinessWatcherDataBase::class.java,
                    "businesswatcher_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}