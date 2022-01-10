package com.example.businesswatcher.project

import android.app.Application
import androidx.room.Database
import com.example.businesswatcher.project.database.BusinessWatcherDataBase
import com.example.businesswatcher.project.database.repository.CompanyRepository
import com.example.businesswatcher.project.database.repository.GroupRepository
import com.example.businesswatcher.project.database.repository.TypeRepository

class App : Application() {

    val database by lazy { BusinessWatcherDataBase.getDatabase(this) }
    val typeRepository by lazy { TypeRepository(database.typeDao()) }
    val groupRepository by lazy { GroupRepository(database.groupDao()) }
    val companyRepository by lazy { CompanyRepository(database.companyDao()) }
}