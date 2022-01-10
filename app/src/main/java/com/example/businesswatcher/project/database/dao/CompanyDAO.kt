package com.example.businesswatcher.project.database.dao

import androidx.room.*
import com.example.businesswatcher.project.models.Company
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDAO {

    @Query("SELECT * FROM company_table")
    fun getCompanies(): Flow<List<Company>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Company)

    @Delete
    suspend fun delete(company: Company)
}