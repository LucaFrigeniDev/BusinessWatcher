package com.example.businesswatcher.project.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.database.repository.TypeRepository
import com.example.businesswatcher.project.models.Type
import kotlinx.coroutines.launch

class TypeViewModel(private val repository: TypeRepository) : ViewModel() {

    val typeList: LiveData<List<Type>> = repository.typeList.asLiveData()

    fun insert(type: Type) = viewModelScope.launch { repository.insert(type) }

    fun delete(type: Type) = viewModelScope.launch { repository.delete(type) }
}

class TypeViewModelFactory(private val repository: TypeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TypeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TypeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}