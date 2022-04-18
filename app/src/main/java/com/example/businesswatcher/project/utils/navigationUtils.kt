package com.example.businesswatcher.project.utils

import android.annotation.SuppressLint
import androidx.navigation.NavController
import com.example.businesswatcher.R
import com.example.businesswatcher.project.ui.fragment.ListCompanyFragmentDirections
import com.example.businesswatcher.project.ui.fragment.ListGroupFragmentDirections
import com.example.businesswatcher.project.ui.fragment.MapsFragmentDirections

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavController
lateinit var displayFragment: String

//TAB
fun groupsToMaps() =
    navController.navigate(ListGroupFragmentDirections.actionListGroupFragmentToMapsFragment())

fun companiesToMaps() =
    navController.navigate(ListCompanyFragmentDirections.actionListCompanyFragmentToMapsFragment())

fun mapsToCompanies() =
    navController.navigate(MapsFragmentDirections.actionMapsFragmentToListCompanyFragment())

fun tabsNavigation(position: Int) {
    when (position) {
        0 -> when (displayFragment) {
            "ListGroupFragment" -> groupsToMaps()
            "ListCompanyFragment" -> companiesToMaps()
        }
        1 -> mapsToCompanies()
    }
}

//List
fun companiesToGroups() =
    navController.navigate(ListCompanyFragmentDirections.actionListCompanyFragmentToListGroupFragment())

fun groupsToCompanies() =
    navController.navigate(ListGroupFragmentDirections.actionListGroupFragmentToListCompanyFragment())

//ADD BTN
fun mapsToGroupCreation() =
    navController.navigate(MapsFragmentDirections.actionMapsFragmentToGroupCreationFragment())

fun groupsToGroupCreation() =
    navController.navigate(ListGroupFragmentDirections.actionListGroupFragmentToGroupCreationFragment())

fun companiesToGroupCreation() =
    navController.navigate(ListCompanyFragmentDirections.actionListCompanyFragmentToGroupCreationFragment())

fun mapsToCompanyCreation() =
    navController.navigate(MapsFragmentDirections.actionMapsFragmentToCompanyCreationFragment())

fun groupsToCompanyCreation() =
    navController.navigate(ListGroupFragmentDirections.actionListGroupFragmentToCompanyCreationFragment())

fun companiesToCompanyCreation() =
    navController.navigate(ListCompanyFragmentDirections.actionListCompanyFragmentToCompanyCreationFragment())

fun createNavigation(menuItemId: Int): Boolean {
    when (menuItemId) {
        R.id.create_group -> {
            when (displayFragment) {
                "MapsFragment" -> mapsToGroupCreation()
                "ListGroupFragment" -> groupsToGroupCreation()
                "ListCompanyFragment" -> companiesToGroupCreation()
            }
            return true
        }
        R.id.create_company -> {
            when (displayFragment) {
                "MapsFragment" -> mapsToCompanyCreation()
                "ListGroupFragment" -> groupsToCompanyCreation()
                "ListCompanyFragment" -> companiesToCompanyCreation()
            }
            return true
        }
        else -> return false
    }
}

//DRAWER
fun mapsToBusinessSectors() =
    navController.navigate(MapsFragmentDirections.actionMapsFragmentToBusinessSectorFragment())

fun groupsToBusinessSectors() =
    navController.navigate(ListGroupFragmentDirections.actionListGroupFragmentToBusinessSectorFragment())

fun companiesToBusinessSectors() =
    navController.navigate(ListCompanyFragmentDirections.actionListCompanyFragmentToBusinessSectorFragment())

fun drawerNavigation() {
    when (displayFragment) {
        "MapsFragment" -> mapsToBusinessSectors()
        "ListGroupFragment" -> groupsToBusinessSectors()
        "ListCompanyFragment" -> companiesToBusinessSectors()
    }
}

//Detail
fun mapToCompanyDetail(id: Long) = navController.navigate(
    MapsFragmentDirections.actionMapsFragmentToCompanyDetailFragment(id)
)





fun listToCompanyDetail(id: Long) = navController.navigate(
    ListCompanyFragmentDirections.actionListCompanyFragmentToCompanyDetailFragment(id)
)

fun groupDetail(id: Long) = navController.navigate(
    ListGroupFragmentDirections.actionListGroupFragmentToGroupDetailFragment(id)
)


