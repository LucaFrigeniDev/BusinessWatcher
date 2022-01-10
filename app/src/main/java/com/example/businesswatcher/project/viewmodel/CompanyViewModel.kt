package com.example.businesswatcher.project.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.database.repository.CompanyRepository
import com.example.businesswatcher.project.models.Company
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    val companyList: LiveData<List<Company>> = repository.companyList.asLiveData()

    fun insert(company: Company) = viewModelScope.launch { repository.insert(company) }

    fun delete(company: Company) = viewModelScope.launch { repository.delete(company) }
}

class CompanyViewModelFactory(private val repository: CompanyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompanyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CompanyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}