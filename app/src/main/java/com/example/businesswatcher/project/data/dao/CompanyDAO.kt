package com.example.businesswatcher.project.data.dao

import androidx.room.*
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.other.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDAO {

    @Query("SELECT * FROM company_table ORDER BY turnover DESC")
    fun getCompaniesSortedByTurnover(): Flow<List<Company>>

    @Query("SELECT * FROM company_table ORDER BY groupId, turnover  ")
    fun getCompaniesSortedByGroup(): Flow<List<Company>>

    @Query("SELECT * FROM company_table ORDER BY businessSectorId, turnover DESC")
    fun getCompaniesSortedBySector(): Flow<List<Company>>

    @Query("SELECT * FROM company_table WHERE id = :id")
    fun getCompany(id: Long): Flow<Company>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Company)

    @Query("UPDATE company_table SET description = :description WHERE id = :id")
    suspend fun updateCompanyDescription(description: String, id: Long)

    @Query("UPDATE company_table SET customers = :customers WHERE id = :id")
    suspend fun updateCompanyCustomers(customers: List<Customer>, id: Long)

    @Delete
    suspend fun delete(company: Company)
}