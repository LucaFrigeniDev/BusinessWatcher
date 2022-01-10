package com.example.businesswatcher.project.database.dao

import androidx.room.*
import com.example.businesswatcher.project.models.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDAO {

    @Query("SELECT * FROM group_table")
    fun getGroups(): Flow<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: Group)

    @Delete
    suspend fun delete(group: Group)
}