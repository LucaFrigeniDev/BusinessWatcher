package com.example.businesswatcher.project.ui.fragment

import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentCompanyCreationBinding
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Group
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.viewmodel.CompanyCreationViewModel
import com.example.businesswatcher.project.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyCreationFragment : Fragment() {

    private var _bind: FragmentCompanyCreationBinding? = null
    private val bind get() = _bind!!

    private val viewModel: CompanyCreationViewModel by viewModels()

    private lateinit var name: String
    private lateinit var businessSectorName: String
    private lateinit var groupName: String
    private lateinit var city: String
    private lateinit var postalCode: String
    private lateinit var address: String
    private lateinit var turnover: String
    private lateinit var description: String

    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var logo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        displayFragment = "CompanyCreationFragment"
        _bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_company_creation, container, false)
        (activity as MainActivity?)!!.setNavigationView(true)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.backBtn.setOnClickListener { findNavController().navigateUp() }
        viewModel.businessSectorList.observe(viewLifecycleOwner) { businessSectorDropdownMenu(it) }
        viewModel.groupList.observe(viewLifecycleOwner) { groupDropdownMenu(it) }
        validationBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)!!.setNavigationView(false)
        _bind = null
    }

    private fun groupDropdownMenu(groups: List<Group>) =
        dropDownMenu(requireContext(), groups, null, bind.groupInput)

    private fun businessSectorDropdownMenu(businessSectors: List<BusinessSector>) =
        dropDownMenu(requireContext(), null, businessSectors, bind.businessSectorInput)

    private fun validationBtn() = bind.validateCompany.setOnClickListener {
        setValues()
        viewModel.isEntryValid(
            name, businessSectorName, groupName, city, postalCode, address, turnover, description
        ).observe(viewLifecycleOwner) { isEntryValid ->
            if (isEntryValid == true) {
                setLatLng()

                viewModel.isCorrectlyGeoLocate(latitude, longitude).observe(viewLifecycleOwner) {
                    if (it == true) {
                        if (groupName != getString(R.string.independent_chip)) createCompany()
                        else setLogo()
                    } else toast(requireContext(), getString(R.string.error_address))
                }
            } else toast(requireContext(), getString(R.string.error_empty_fields))
        }
    }

    private fun setValues() {
        name = bind.nameET.text.toString()
        businessSectorName = bind.businessSectorInput.text.toString()
        groupName = bind.groupInput.text.toString()
        city = bind.cityET.text.toString()
        postalCode = bind.PCET.text.toString()
        address = bind.addressET.text.toString()
        turnover = bind.turnoverET.text.toString()
        description = bind.descriptionET.text.toString()
    }

    private fun setLatLng() {
        val location = "$address, $city $postalCode"
        latitude = geoLocate(location, requireContext())?.latitude
        longitude = geoLocate(location, requireContext())?.longitude
    }

    private fun createCompany() =
        viewModel.getBusinessSectorId(bind.businessSectorInput.text.toString())
            .observe(viewLifecycleOwner) { businessSectorId ->
                if (groupName != getString(R.string.independent_chip)) {
                    viewModel.getGroupId(groupName).observe(viewLifecycleOwner) { groupId ->
                        viewModel.getGroupLogo(groupId).observe(viewLifecycleOwner) { logo ->
                            insert(businessSectorId, groupId, logo)
                        }
                    }
                } else insert(businessSectorId, null, logo)
            }

    private fun insert(businessSectorId: Long, groupId: Long?, logo: String) {
        viewModel.createCompany(
            name,
            businessSectorId,
            groupId,
            city,
            postalCode,
            address,
            latitude!!,
            longitude!!,
            turnover,
            description,
            logo
        )
        findNavController().navigateUp()
    }

    private fun setLogo() = MaterialAlertDialogBuilder(requireContext())
        .setMessage(getString(R.string.logo_alert_dialog_text))
        .setPositiveButton(getString(R.string.import_logo)) { _, _ ->
            if (isFileAccessAllowed(requireContext())) resultLauncher.launch(pickPhoto())
            else askFileAccessPermission(requireActivity())
        }
        .show()

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) addPhoto(result.data?.data!!)
            else toast(requireContext(), getString(R.string.error_pick_file))
        }

    private fun addPhoto(uri: Uri) = viewModel.uploadImage(uri).observe(viewLifecycleOwner) {
        logo = it
        createCompany()
    }
}