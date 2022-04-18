package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.*
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.entities.Group
import com.example.businesswatcher.project.domain.other.GroupBusinessSector
import com.example.businesswatcher.project.domain.other.GroupRank
import com.example.businesswatcher.project.domain.relations.BusinessSectorWithCompanies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel
@Inject constructor(
    private val groupRepository: GroupRepository,
    private val businessSectorRepository: BusinessSectorRepository
) : ViewModel() {

    private val _companies: MutableLiveData<List<BusinessSectorWithCompanies>> =
        MutableLiveData(mutableListOf())
    val companies: LiveData<List<BusinessSectorWithCompanies>> = _companies

    private val _name: MutableLiveData<String> = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _logo: MutableLiveData<String> = MutableLiveData("")
    val logo: LiveData<String> = _logo

    private val _description: MutableLiveData<String> = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _turnover: MutableLiveData<String> = MutableLiveData("")
    val turnover: LiveData<String> = _turnover

    private val _rank: MutableLiveData<String> = MutableLiveData("")
    val rank: LiveData<String> = _rank

    private val _numberOfCompanies: MutableLiveData<String> = MutableLiveData("")
    val numberOfCompanies: LiveData<String> = _numberOfCompanies

    fun getGroup(id: Long): LiveData<Group> {
        setValues(id)
        return groupRepository.getGroup(id).asLiveData()
    }

    private fun setValues(id: Long) {
        groupRepository.groupWithCompaniesList.onEach { groupsWitchCompanies ->
            val groupRanks: MutableList<GroupRank> = mutableListOf()
            for (groupWithCompanies in groupsWitchCompanies) {
                val turnover = groupWithCompanies.companies.sumOf { company -> company.turnover }
                val groupRank = GroupRank(groupWithCompanies.group.id, turnover)
                groupRanks.add(groupRank)

                if (groupWithCompanies.group.id == id) {
                    _name.postValue(groupWithCompanies.group.name)
                    _description.postValue(groupWithCompanies.group.description)
                    _logo.postValue(groupWithCompanies.group.logo)
                    _numberOfCompanies.postValue(groupWithCompanies.companies.size.toString())
                    _turnover.postValue(turnover.toString())
                }
            }
            groupRanks.sortByDescending { group -> group.turnover }
            _rank.postValue((groupRanks.indexOf(groupRanks.find { group -> group.id == id }) + 1).toString())

            businessSectorRepository.getBusinessSectorWithCompanies().onEach {
                val businessSectorWithCompanies: MutableList<BusinessSectorWithCompanies> =
                    mutableListOf()

                for (businessSectors in it) {
                    val businessSector =
                        BusinessSectorWithCompanies(businessSectors.businessSector, mutableListOf())

                    for (company in businessSectors.companies) {
                        if (company.groupId == id)
                            businessSector.companies.add(company)
                    }
                    businessSectorWithCompanies.add(businessSector)
                }

                _companies.postValue(businessSectorWithCompanies)
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    fun businessSectors(id: Long): MutableLiveData<List<GroupBusinessSector>> {
        val groupBusinessSectors: MutableLiveData<List<GroupBusinessSector>> =
            MutableLiveData(mutableListOf())

        businessSectorRepository.getBusinessSectorWithCompanies().onEach { list ->
            groupRepository.groups.onEach { groups ->
                val groupBusinessSectorList: MutableList<GroupBusinessSector> = mutableListOf()
                for (businessSectorWithCompanies in list) {
                    val companies: MutableList<Company> = mutableListOf()
                    for (company in businessSectorWithCompanies.companies) {
                        if (company.groupId == id)
                            companies.add(company)
                    }

                    val sectorGlobalTurnover =
                        businessSectorWithCompanies.companies.sumOf { it.turnover }
                    val sectorGroupTurnover = companies.sumOf { it.turnover }
                    var share = 0
                    if (sectorGlobalTurnover != 0) {
                        share = sectorGroupTurnover * 100 / sectorGlobalTurnover
                    }

                    if (companies.isNotEmpty()) {
                        groupBusinessSectorList.add(
                            GroupBusinessSector(
                                businessSectorWithCompanies.businessSector.name,
                                businessSectorWithCompanies.businessSector.color,
                                companies.size.toString(),
                                sectorGroupTurnover.toString(),
                                getBusinessSectorRank(
                                    businessSectorWithCompanies,
                                    groups,
                                    id
                                ).toString(),
                                "$share%"
                            )
                        )
                    }
                }
                groupBusinessSectors.postValue(groupBusinessSectorList)
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
        return groupBusinessSectors
    }

    private fun getBusinessSectorRank(
        businessSectorWithCompanies: BusinessSectorWithCompanies, groups: List<Group>, id: Long
    ): Int {
        val groupRanks: MutableList<GroupRank> = mutableListOf()

        for (company in businessSectorWithCompanies.companies) {
            if (company.groupId == null) {
                groupRanks.add(GroupRank(company.id, company.turnover))
            }
        }

        for (group in groups) {
            val companies: MutableList<Company> = mutableListOf()
            for (company in businessSectorWithCompanies.companies) {
                if (company.groupId == group.id) {
                    companies.add(company)
                }
            }

            if (companies.isNotEmpty()) {
                groupRanks.add(
                    GroupRank(
                        companies[0].groupId!!.toLong(),
                        companies.sumOf { it.turnover }
                    )
                )
            }
        }
        groupRanks.sortByDescending { it.turnover }
        return groupRanks.indexOf(groupRanks.find { it.id == id }) + 1
    }

    fun updateDescription(description: String, id: Long) =
        viewModelScope.launch { groupRepository.updateDescription(description, id) }
}