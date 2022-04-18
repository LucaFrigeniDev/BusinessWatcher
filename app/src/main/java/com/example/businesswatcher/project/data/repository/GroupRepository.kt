package com.example.businesswatcher.project.data.repository

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.businesswatcher.project.data.dao.GroupDAO
import com.example.businesswatcher.project.domain.entities.Group
import com.example.businesswatcher.project.domain.relations.GroupWithCompanies
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class GroupRepository @Inject constructor(val dao: GroupDAO) {

    val groups: Flow<List<Group>> = dao.getGroups()

    val groupWithCompaniesList: Flow<List<GroupWithCompanies>> = dao.getGroupWithCompanies()

    fun getGroup(id: Long): Flow<Group> = dao.getGroup(id)

    fun getGroupId(name: String): Flow<Long> = dao.getGroupId(name)

    fun getGroupLogo(id: Long): Flow<String> = dao.getGroupLogo(id)

    @WorkerThread
    suspend fun insert(group: Group) = dao.insert(group)

    @WorkerThread
    suspend fun updateDescription(description: String, id: Long) =
        dao.updateGroupDescription(description, id)

    fun uploadImage(imageUri: Uri?): LiveData<String> {
        val data = MutableLiveData<String>()
        val uuid: String = UUID.randomUUID().toString()
        val mImageRef = FirebaseStorage.getInstance().getReference(uuid)
        mImageRef.putFile(imageUri!!)
            .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                if (task.isSuccessful) data.value = uuid
            }
        return data
    }
}