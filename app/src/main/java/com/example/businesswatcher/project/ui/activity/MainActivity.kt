package com.example.businesswatcher.project.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.businesswatcher.R
import com.example.businesswatcher.project.ui.fragment.ListFragment
import com.example.businesswatcher.project.ui.fragment.MapsFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(MapsFragment())
        setTabLayout()
        setBottomAppBar()
        setNavigationView()
        setAddButton()
    }

    fun setFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).commit()
    }

    fun setTabLayout(){
        findViewById<TabLayout>(R.id.tab_layout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> setFragment(MapsFragment())
                    1 -> setFragment(ListFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> setFragment(MapsFragment())
                    1 -> setFragment(ListFragment())
                }
            }
        })
    }

    fun setAddButton(){
        findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener {
                v: View -> showMenu(v, R.menu.popup_menu)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {

                R.id.create_group -> {
                    startActivity(Intent(this, GroupCreationActivity::class.java))
                    true
                }

                R.id.create_company -> {
                    startActivity(Intent(this, CompanyCreationActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    fun setBottomAppBar(){
        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById<NestedScrollView>(R.id.bottom_sheet))
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

        findViewById<BottomAppBar>(R.id.bottom_app_bar).setNavigationOnClickListener {
            findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }

        findViewById<BottomAppBar>(R.id.bottom_app_bar).setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    true
                }
                else -> false
            }
        }
    }

    fun setNavigationView(){
        findViewById<NavigationView>(R.id.navigation_view).setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.type -> {
                    startActivity(Intent(this, TypeActivity::class.java))
                    true
                }
                R.id.export_Json -> {
                    true
                }
                R.id.import_Json -> {
                    true
                }
                else -> false
            }
        }
    }

}

