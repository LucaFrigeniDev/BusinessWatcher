package com.example.businesswatcher.project.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.NavHostFragment
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.ActivityMainBinding
import com.example.businesswatcher.project.ui.viewmodel.MainActivityViewModel
import com.example.businesswatcher.project.utils.*
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var businessSectorChipGroup: ChipGroup
    private lateinit var groupChipGroup: ChipGroup

    lateinit var filterBtn: Button
    lateinit var clearBtn: Button
    private lateinit var searchBar: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomSheetBehavior =
            BottomSheetBehavior.from(findViewById<NestedScrollView>(R.id.bottom_sheet))

        businessSectorChipGroup = findViewById(R.id.sector_chipGroup)
        groupChipGroup = findViewById(R.id.group_chipGroup)
        filterBtn = findViewById(R.id.filter_btn)
        clearBtn = findViewById(R.id.clear_btn)
        searchBar = findViewById(R.id.company_filter_et)

        addBtn()
        tabLayout()
        bottomAppBar()
        bottomSheet()
        navigationDrawerMenu()
    }

    override fun onStart() {
        super.onStart()
        if (!isLocationAllowed(this)) askLocationPermission(this)
        else getDeviceLocation(this)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    fun setNavigationView(isVisible: Boolean) =
        if (isVisible) {
            bind.tabLayout.visibility = View.GONE
            bind.bottomAppBar.visibility = View.GONE
            bind.addBtn.visibility = View.GONE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bind.tabLayout.visibility = View.VISIBLE
            bind.bottomAppBar.visibility = View.VISIBLE
            bind.addBtn.visibility = View.VISIBLE
        }

    private fun tabLayout() =
        bind.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabsNavigation(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    private fun addBtn() = viewModel.isBusinessSectorListEmpty().observe(this) {
        bind.addBtn.setOnClickListener { v ->
            when (it) {
                true -> toast(this, getString(R.string.error_no_sector))
                false -> showMenu(v, R.menu.popup_menu_add)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem -> createNavigation(menuItem.itemId) }
        popup.show()
    }

    private fun bottomAppBar() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bind.bottomAppBar.setNavigationOnClickListener {
            bind.drawerLayout.openDrawer(GravityCompat.START)
        }

        bind.bottomAppBar.setOnMenuItemClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            true
        }
    }

    private fun bottomSheet() {
        viewModel.getBusinessSectors.observe(this) {
            setChipGroup(businessSectorChipGroup, it, null, this)
        }

        viewModel.getGroups.observe(this) {
            setChipGroup(groupChipGroup, null, it, this)
        }

        findViewById<Button>(R.id.hide_btn).setOnClickListener { hideBottomSheet() }
    }

    fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setFilters() = viewModel.setFilters(
        businessSectorChipGroup.checkedChipIds,
        groupChipGroup.checkedChipIds,
        searchBar.text.toString()
    )

    fun clear() {
        bottomSheet()
        searchBar.text!!.clear()
        setFilters()
    }

    private fun navigationDrawerMenu() = bind.navigationView.setNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.business_sector -> {
                drawerNavigation()
                bind.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.export_Json -> true
            R.id.import_Json -> true
            else -> false
        }
    }
}
