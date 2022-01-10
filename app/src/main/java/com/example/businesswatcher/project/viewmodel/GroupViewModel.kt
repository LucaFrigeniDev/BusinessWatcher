package com.example.businesswatcher.project.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.database.repository.GroupRepository
import com.example.businesswatcher.project.models.Group
import kotlinx.coroutines.launch

class GroupViewModel(private val repository: GroupRepository) : ViewModel() {

    val groupList: LiveData<List<Group>> = repository.groupList.asLiveData()

    fun insert(group: Group) = viewModelScope.launch { repository.insert(group) }

    fun delete(group: Group) = viewModelScope.launch { repository.delete(group) }
}

class GroupViewModelFactory(private val repository: GroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}