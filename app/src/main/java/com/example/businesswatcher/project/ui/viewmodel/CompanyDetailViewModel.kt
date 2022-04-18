package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.data.repository.CompanyRepository
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.other.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyDetailViewModel
@Inject constructor(
    private val companyRepository: CompanyRepository,
    private val businessSectorRepository: BusinessSectorRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private var _company: MutableLiveData<Company> = MutableLiveData(null)
    val company: LiveData<Company> = _company

    private var _groupName: MutableLiveData<String> = MutableLiveData("")
    val groupName: LiveData<String> = _groupName

    private var _businessSectorName: MutableLiveData<String> = MutableLiveData("")
    val businessSectorName: LiveData<String> = _businessSectorName

    fun getCompany(companyId: Long) = companyRepository.getCompany(companyId).onEach {
        _company.postValue(it)

        getBusinessSectorName(it.businessSectorId)

        if (it.groupId != null) {
            getGroupName(it.groupId!!)
        } else _groupName.postValue("Independent")

    }.launchIn(viewModelScope)

    private fun getGroupName(id: Long) =
        groupRepository.getGroup(id).onEach {
            _groupName.postValue(it.name)
        }.launchIn(viewModelScope)

    private fun getBusinessSectorName(id: Long) =
        businessSectorRepository.getBusinessSector(id).onEach {
            _businessSectorName.postValue(it.name)
        }.launchIn(viewModelScope)

    fun updateDescription(description: String, id: Long) =
        viewModelScope.launch { companyRepository.updateDescription(description, id) }

    fun addCustomer(customers: List<Customer>, customer: Customer, id: Long) {
        val customerList: MutableList<Customer> = mutableListOf()
        customerList.addAll(customers)
        customerList.add(customer)

        viewModelScope.launch {
            companyRepository.updateCustomers(customerList as List<Customer>, id)
        }
    }
}