package com.example.businesswatcher.project.data.repository

import androidx.annotation.WorkerThread
import com.example.businesswatcher.project.data.dao.CompanyDAO
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.other.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(val dao: CompanyDAO) {

    fun getCompany(id: Long): Flow<Company> = dao.getCompany(id)

    @WorkerThread
    suspend fun insert(company: Company) = dao.insert(company)

    @WorkerThread
    suspend fun updateDescription(description: String, id: Long) =
        dao.updateCompanyDescription(description, id)

    @WorkerThread
    suspend fun updateCustomers(customers: List<Customer>, id: Long) =
        dao.updateCompanyCustomers(customers, id)
}