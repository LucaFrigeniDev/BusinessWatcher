package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentListCompanyBinding
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.adapter.CompanyAdapter
import com.example.businesswatcher.project.ui.viewmodel.CompanyListViewModel
import com.example.businesswatcher.project.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCompanyFragment : Fragment() {

    private var _bind: FragmentListCompanyBinding? = null
    private val bind get() = _bind!!

    private val viewModel: CompanyListViewModel by viewModels()
    private lateinit var currentSortType: SortType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        displayFragment = "ListCompanyFragment"
        _bind = DataBindingUtil
            .inflate(inflater, R.layout.fragment_list_company, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter(SortType.TURNOVER)
        switchBtn()
        sortBtn()
        filter()
        clearFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    private fun filter() = (activity as MainActivity).filterBtn.setOnClickListener {
        (activity as MainActivity).setFilters()
        (activity as MainActivity).hideBottomSheet()
        adapter(currentSortType)
    }

    private fun clearFilter() = (activity as MainActivity).clearBtn.setOnClickListener {
        (activity as MainActivity).clear()
        adapter(currentSortType)
    }

    private fun adapter(sortType: SortType) =
        viewModel.getBusinessSectorsWithCompanies().observe(viewLifecycleOwner) {
            val companies: MutableList<Company> = mutableListOf()
            val businessSectors: MutableList<BusinessSector> = mutableListOf()

            for (businessSector in it) {
                companies.addAll(businessSector.companies)
                businessSectors.add(businessSector.businessSector)
            }

            when (sortType) {
                SortType.TURNOVER -> companies.sortByDescending { company -> company.turnover }
                SortType.GROUP -> companies.sortBy { company -> company.groupId }
                SortType.SECTOR -> companies.sortBy { company -> company.businessSectorId }
                SortType.DISTANCE -> companies.sortBy { company ->
                    getDistance(company.latitude, company.longitude)
                }
            }

            currentSortType = sortType

            bind.recyclerView.adapter = CompanyAdapter(companies, businessSectors)

            if (companies.isEmpty()) bind.emptyLayout.visibility = View.VISIBLE
            else bind.emptyLayout.visibility = View.GONE
        }

    private fun sortBtn() = bind.sortBtn.setOnClickListener { showMenu(it, R.menu.popup_menu_sort) }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.turnover_sort -> {
                    adapter(SortType.TURNOVER)
                    true
                }
                R.id.group_sort -> {
                    adapter(SortType.GROUP)
                    true
                }
                R.id.business_sector_sort -> {
                    adapter(SortType.SECTOR)
                    true
                }
                R.id.distance_sort -> {
                    if (userLocation != null) {
                        adapter(SortType.DISTANCE)
                    } else toast(requireContext(), getString(R.string.error_geolocation))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun switchBtn() = bind.changeBtn.setOnClickListener { companiesToGroups() }
}