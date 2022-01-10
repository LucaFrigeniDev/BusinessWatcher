package com.example.businesswatcher.project.database.repository

import androidx.annotation.WorkerThread
import com.example.businesswatcher.project.database.dao.GroupDAO
import com.example.businesswatcher.project.models.Group
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val dao: GroupDAO) {

    val groupList: Flow<List<Group>> = dao.getGroups()

    @WorkerThread
    suspend fun insert(group: Group) = dao.insert(group)

    @WorkerThread
    suspend fun delete(group: Group) = dao.delete(group)
}