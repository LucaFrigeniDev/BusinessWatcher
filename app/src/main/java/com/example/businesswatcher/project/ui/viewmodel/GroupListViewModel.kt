package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.domain.relations.GroupWithCompanies
import com.example.businesswatcher.project.utils.groupFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel
@Inject constructor(val repository: GroupRepository) : ViewModel() {

    fun getGroups(): LiveData<List<GroupWithCompanies>> {
        return if (groupFilter.isNotEmpty()) {
            val groups: MutableLiveData<List<GroupWithCompanies>> = MutableLiveData(mutableListOf())
            repository.groupWithCompaniesList.onEach {
                val filteredList: MutableList<GroupWithCompanies> = mutableListOf()
                for (group in it) {
                    if (groupFilter.contains(group.group.id.toString())) {
                        filteredList.add(group)
                    }
                }
                groups.postValue(filteredList)
            }.launchIn(viewModelScope)
            groups
        } else {
            repository.groupWithCompaniesList.asLiveData()
        }
    }

}