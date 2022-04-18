package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentBusinessSectorBinding
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.adapter.BusinessSectorAdapter
import com.example.businesswatcher.project.ui.viewmodel.BusinessSectorViewModel
import com.example.businesswatcher.project.utils.dialogBuilder
import com.example.businesswatcher.project.utils.displayFragment
import com.example.businesswatcher.project.utils.toast
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessSectorFragment : Fragment() {

    private var _bind: FragmentBusinessSectorBinding? = null
    private val bind get() = _bind!!

    private val viewModel: BusinessSectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity?)!!.setNavigationView(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        displayFragment = "BusinessSectorFragment"
        _bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_business_sector, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.backBtn.setOnClickListener { findNavController().navigateUp() }

        viewModel.getBusinessSectorWithCompanies().observe(viewLifecycleOwner) {
            bind.businessSectorList.adapter = BusinessSectorAdapter(it)
        }

        viewModel.businessSectorsInDataBase.observe(viewLifecycleOwner) { numberOfBusinessSector ->
            if (numberOfBusinessSector == 0) bind.emptyLayout.visibility = View.VISIBLE
            else bind.emptyLayout.visibility = View.GONE

            if (numberOfBusinessSector == 5) bind.addSectorBtn.visibility = View.GONE
            bind.addSectorBtn.setOnClickListener { showAddAlertDialog(numberOfBusinessSector) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)!!.setNavigationView(false)
        _bind = null
    }

    private fun showAddAlertDialog(businessSectors: Int) {
        val customAlertDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_business_sector, null, false)

        val nameTextField: TextInputLayout = customAlertDialogView.findViewById(R.id.name_layout)

        dialogBuilder(requireContext(), "Sector")
            .setView(customAlertDialogView)
            .setPositiveButton(getString(R.string.add_sector_dialog_positive_btn)) { dialog, _ ->
                val name = nameTextField.editText?.text.toString()
                if (name.isNotBlank()) {
                    createBusinessSector(businessSectors, name)
                    dialog.dismiss()
                } else toast(requireContext(), getString(R.string.sector_name_error))
            }.show()
    }

    private fun createBusinessSector(businessSectors: Int, name: String) =
        viewModel.createBusinessSector(name, businessSectors)
}