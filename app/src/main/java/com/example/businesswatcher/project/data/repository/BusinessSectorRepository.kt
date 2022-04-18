package com.example.businesswatcher.project.data.repository

import androidx.annotation.WorkerThread
import com.example.businesswatcher.project.data.dao.BusinessSectorDAO
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.relations.BusinessSectorWithCompanies
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BusinessSectorRepository @Inject constructor(val dao: BusinessSectorDAO) {

    val businessSectorList: Flow<List<BusinessSector>> = dao.getBusinessSectors()

    fun getBusinessSectorWithCompanies(): Flow<List<BusinessSectorWithCompanies>> =
        dao.getBusinessSectorWithCompanies()

    val businessSectorListSize: Flow<Int> = dao.getBusinessSectorListSize()

    fun getBusinessSectorId(name: String): Flow<Long> = dao.getBusinessSectorId(name)

    fun getBusinessSector(id: Long): Flow<BusinessSector> = dao.getBusinessSector(id)

    @WorkerThread
    suspend fun insert(businessSector: BusinessSector) = dao.insert(businessSector)
}