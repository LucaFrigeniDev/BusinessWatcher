package com.example.businesswatcher.project.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.data.repository.CompanyRepository
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.entities.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyCreationViewModel
@Inject constructor(
    private val companyRepository: CompanyRepository,
    private val businessSectorRepository: BusinessSectorRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    val businessSectorList: LiveData<List<BusinessSector>> =
        businessSectorRepository.businessSectorList.asLiveData()

    val groupList: LiveData<List<Group>> = groupRepository.groups.asLiveData()

    fun isEntryValid(
        name: String,
        businessSector: String,
        group: String,
        city: String,
        postalCode: String,
        address: String,
        turnover: String,
        description: String
    ): MutableLiveData<Boolean> = MutableLiveData(
        name.isNotBlank() &&
                businessSector.isNotBlank() &&
                group.isNotBlank() &&
                city.isNotBlank() &&
                postalCode.isNotBlank() &&
                address.isNotBlank() &&
                turnover.isNotBlank() &&
                description.isNotBlank()
    )


    fun isCorrectlyGeoLocate(latitude: Double?, longitude: Double?): MutableLiveData<Boolean> =
        MutableLiveData(latitude != null && longitude != null)

    fun getBusinessSectorId(name: String) =
        businessSectorRepository.getBusinessSectorId(name).asLiveData()

    fun getGroupId(name: String) = groupRepository.getGroupId(name).asLiveData()

    fun getGroupLogo(id: Long) = groupRepository.getGroupLogo(id).asLiveData()

    fun createCompany(
        name: String,
        businessSectorId: Long,
        groupId: Long?,
        city: String,
        postalCode: String,
        address: String,
        latitude: Double,
        longitude: Double,
        turnover: String,
        description: String,
        logo: String
    ) = viewModelScope.launch {
        companyRepository.insert(
            Company(
                0,
                name,
                businessSectorId,
                groupId,
                city,
                postalCode,
                address,
                latitude,
                longitude,
                turnover.toInt(),
                arrayListOf(),
                description,
                logo
            )
        )
    }

    fun uploadImage(uri: Uri?): LiveData<String> = groupRepository.uploadImage(uri)


    //fun create(
    //    name: String,
    //    businessSector: String,
    //    group: String,
    //    city: String,
    //    postalCode: String,
    //    address: String,
    //    turnover: String,
    //    description: String,
    //    logo: String
    //): MutableLiveData<String> {
    //    val st = MutableLiveData("")
    //    if (isEntryValidA(
    //            name, businessSector, group, city, postalCode, address, turnover, description
    //        )
    //    ) {
    //        val location = "$address, $city $postalCode"
    //        val latitude = geoLocate(location, context)?.latitude
    //        val longitude = geoLocate(location, context)?.longitude
//
    //        if (isCorrectlyGeoLocateA(latitude, longitude)) {
    //            if (group != "Independent") {
    //                businessSectorRepository.getBusinessSectorId(name).onEach { businessSectorId ->
    //                    groupRepository.getGroupId(name).onEach { groupId ->
    //                        groupRepository.getGroupLogo(groupId).onEach { groupLogo ->
    //                            companyRepository.insert(
    //                                Company(
    //                                    0,
    //                                    name,
    //                                    businessSectorId,
    //                                    groupId,
    //                                    city,
    //                                    postalCode,
    //                                    address,
    //                                    latitude!!,
    //                                    longitude!!,
    //                                    turnover.toInt(),
    //                                    arrayListOf(),
    //                                    description,
    //                                    groupLogo
    //                                )
    //                            )
    //                        }.launchIn(viewModelScope)
    //                    }.launchIn(viewModelScope)
    //                }.launchIn(viewModelScope)
    //            } else {
    //                if (logo.isNotBlank()) {
    //                    businessSectorRepository.getBusinessSectorId(name).onEach {
    //                        companyRepository.insert(
    //                            Company(
    //                                0,
    //                                name,
    //                                it,
    //                                null,
    //                                city,
    //                                postalCode,
    //                                address,
    //                                latitude!!,
    //                                longitude!!,
    //                                turnover.toInt(),
    //                                arrayListOf(),
    //                                description,
    //                                logo
    //                            )
    //                        )
    //                        st.postValue("OK")
    //                    }.launchIn(viewModelScope)
    //                } else st.postValue("photo")
    //            }
    //        } else st.postValue("geolocation")
    //    } else st.postValue("entry")
    //    return st
    //}
//
    //private fun isEntryValid(
    //    name: String,
    //    businessSector: String,
    //    group: String,
    //    city: String,
    //    postalCode: String,
    //    address: String,
    //    turnover: String,
    //    description: String
    //): Boolean =
    //            name.isNotBlank() &&
    //                businessSector.isNotBlank() &&
    //                group.isNotBlank() &&
    //                city.isNotBlank() &&
    //                postalCode.isNotBlank() &&
    //                address.isNotBlank() &&
    //                turnover.isNotBlank() &&
    //                description.isNotBlank()
//
    //fun isCorrectlyGeoLocate(latitude: Double?, longitude: Double?): Boolean =
    //    latitude != null && longitude != null
}