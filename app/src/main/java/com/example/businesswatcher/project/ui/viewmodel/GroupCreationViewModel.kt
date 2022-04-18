package com.example.businesswatcher.project.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesswatcher.project.data.repository.GroupRepository
import com.example.businesswatcher.project.domain.entities.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreationViewModel
@Inject constructor(private val repository: GroupRepository) : ViewModel() {

    fun createGroup(name: String, logo: String, description: String): MutableLiveData<Boolean> =
        if (isEntryValid(name, description, logo)) {
            viewModelScope.launch { repository.insert(Group(0, name, logo, description)) }
            MutableLiveData(true)
        } else MutableLiveData(false)

    private fun isEntryValid(name: String, description: String, logo: String): Boolean =
        name.isNotBlank() || logo.isNotBlank() || description.isNotBlank()

    fun uploadImage(uri: Uri?): LiveData<String> = repository.uploadImage(uri)
}