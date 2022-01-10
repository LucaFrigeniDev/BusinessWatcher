package com.example.businesswatcher.project.database.repository

import androidx.annotation.WorkerThread
import com.example.businesswatcher.project.database.dao.TypeDAO
import com.example.businesswatcher.project.models.Type
import kotlinx.coroutines.flow.Flow

class TypeRepository(private val dao: TypeDAO) {

    val typeList: Flow<List<Type>> = dao.getTypes()

    @WorkerThread
    suspend fun insert(type: Type) = dao.insert(type)

    @WorkerThread
    suspend fun delete(type: Type) = dao.delete(type)
}