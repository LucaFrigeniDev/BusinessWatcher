package com.example.businesswatcher.project.database.dao

import androidx.room.*
import com.example.businesswatcher.project.models.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDAO {

    @Query("SELECT * FROM type_table")
    fun getTypes(): Flow<List<Type>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: Type)

    @Delete
    suspend fun delete(type: Type)
}