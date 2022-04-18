package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.relations.BusinessSectorWithCompanies
import com.example.businesswatcher.project.utils.businessSectorFilter
import com.example.businesswatcher.project.utils.companyFilter
import com.example.businesswatcher.project.utils.groupFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CompanyListViewModel
@Inject constructor(val businessSectorRepository: BusinessSectorRepository) : ViewModel() {

    fun getBusinessSectorsWithCompanies(): LiveData<List<BusinessSectorWithCompanies>> {
        val businessSectorWithCompanies: MutableLiveData<List<BusinessSectorWithCompanies>> =
            MutableLiveData(mutableListOf())

        if (companyFilter.isNotBlank()) {
            businessSectorRepository.getBusinessSectorWithCompanies()
                .onEach { businessSectors ->
                    val filteredList: MutableList<BusinessSectorWithCompanies> = mutableListOf()
                    for (businessSector in businessSectors) {
                        val newCompanies: MutableList<Company> = mutableListOf()
                        for (company in businessSector.companies) {
                            if (company.name.contains(companyFilter, true)) {
                                newCompanies.add(company)
                            }
                        }
                        businessSector.companies.clear()
                        businessSector.companies.addAll(newCompanies)
                        filteredList.add(businessSector)
                    }

                    businessSectorWithCompanies.postValue(filteredList)
                }.launchIn(viewModelScope)

            return businessSectorWithCompanies

        } else {
            if (businessSectorFilter.isNotEmpty() && groupFilter.isNotEmpty()) {
                businessSectorRepository.getBusinessSectorWithCompanies()
                    .onEach { businessSectors ->
                        val filteredList: MutableList<BusinessSectorWithCompanies> = mutableListOf()
                        for (businessSector in businessSectors) {
                            if (businessSectorFilter.contains(businessSector.businessSector.id.toString())) {
                                val newCompanies: MutableList<Company> = mutableListOf()
                                for (company in businessSector.companies) {
                                    if (groupFilter.contains(company.groupId.toString())) {
                                        newCompanies.add(company)
                                    }

                                    if (groupFilter.contains("0")) {
                                        if (company.groupId == null) {
                                            newCompanies.add(company)
                                        }
                                    }
                                }
                                businessSector.companies.clear()
                                businessSector.companies.addAll(newCompanies)
                                filteredList.add(businessSector)
                            }
                        }
                        businessSectorWithCompanies.postValue(filteredList)
                    }.launchIn(viewModelScope)

                return businessSectorWithCompanies

            } else if (businessSectorFilter.isNotEmpty() && groupFilter.isEmpty()) {
                businessSectorRepository.getBusinessSectorWithCompanies()
                    .onEach { businessSectors ->
                        val filteredList: MutableList<BusinessSectorWithCompanies> = mutableListOf()
                        for (businessSector in businessSectors) {
                            if (businessSectorFilter.contains(businessSector.businessSector.id.toString())) {
                                filteredList.add(businessSector)
                            }
                        }
                        businessSectorWithCompanies.postValue(filteredList)
                    }.launchIn(viewModelScope)

                return businessSectorWithCompanies

            } else if (businessSectorFilter.isEmpty() && groupFilter.isNotEmpty()) {
                businessSectorRepository.getBusinessSectorWithCompanies()
                    .onEach { businessSectors ->
                        val filteredList: MutableList<BusinessSectorWithCompanies> = mutableListOf()
                        for (businessSector in businessSectors) {
                            val newCompanies: MutableList<Company> = mutableListOf()
                            for (company in businessSector.companies) {
                                if (groupFilter.contains(company.groupId.toString())) {
                                    newCompanies.add(company)
                                }

                                if (groupFilter.contains("0")) {
                                    if (company.groupId == null) {
                                        newCompanies.add(company)
                                    }
                                }
                            }
                            businessSector.companies.clear()
                            businessSector.companies.addAll(newCompanies)
                            filteredList.add(businessSector)
                        }
                        businessSectorWithCompanies.postValue(filteredList)
                    }.launchIn(viewModelScope)

                return businessSectorWithCompanies

            } else {
                return businessSectorRepository.getBusinessSectorWithCompanies().asLiveData()
            }
        }
    }
}