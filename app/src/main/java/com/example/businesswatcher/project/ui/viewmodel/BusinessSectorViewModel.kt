package com.example.businesswatcher.project.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.businesswatcher.project.data.repository.BusinessSectorRepository
import com.example.businesswatcher.project.domain.entities.BusinessSector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessSectorViewModel
@Inject constructor(private val repository: BusinessSectorRepository) : ViewModel() {

    val businessSectorsInDataBase = repository.businessSectorListSize.asLiveData()

    fun getBusinessSectorWithCompanies() = repository.getBusinessSectorWithCompanies().asLiveData()

    fun createBusinessSector(name: String, businessSectors: Int) =
        viewModelScope.launch {
            repository.insert(BusinessSector(0, name, setColor(businessSectors)))
        }

    private fun setColor(businessSectors: Int): String {
        lateinit var color: String
        when (businessSectors) {
            0 -> color = "#f54242"
            1 -> color = "#ff8636"
            2 -> color = "#8ce66e"
            3 -> color = "#6ebee6"
            4 -> color = "#ffc830"
        }
        return color
    }
}