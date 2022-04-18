package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.utils.businessSectorFilter
import com.example.businesswatcher.project.utils.companyFilter
import com.example.businesswatcher.project.utils.groupFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(
    private val businessSectorRepository: BusinessSectorRepository,
    groupRepository: GroupRepository
) : ViewModel() {

    val getBusinessSectors = businessSectorRepository.businessSectorList.asLiveData()
    val getGroups = groupRepository.groups.asLiveData()

    fun isBusinessSectorListEmpty(): MutableLiveData<Boolean> {
        val isEmpty: MutableLiveData<Boolean> = MutableLiveData(true)

        businessSectorRepository.businessSectorListSize.onEach {
            isEmpty.postValue(it == 0)
        }.launchIn(viewModelScope)

        return isEmpty
    }

    fun setFilters(businessSectors: List<Int>, groups: List<Int>, companies: String) {
        businessSectorFilter.clear()
        groupFilter.clear()
        companyFilter = ""

        if (businessSectors.isNotEmpty())
            for (id in businessSectors) businessSectorFilter.add(id.toString())

        if (groups.isNotEmpty())
            for (id in groups) groupFilter.add(id.toString())

        if (companies.isNotBlank()) companyFilter = companies
    }
}