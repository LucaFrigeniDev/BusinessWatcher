package com.example.businesswatcher.project.database.repository

import androidx.annotation.WorkerThread
import com.example.businesswatcher.project.database.dao.CompanyDAO
import com.example.businesswatcher.project.models.Company
import kotlinx.coroutines.flow.Flow

class CompanyRepository(private val dao: CompanyDAO) {

    val companyList: Flow<List<Company>> = dao.getCompanies()

    @WorkerThread
    suspend fun insert(company: Company) = dao.insert(company)

    @WorkerThread
    suspend fun delete(company: Company) = dao.delete(company)
}