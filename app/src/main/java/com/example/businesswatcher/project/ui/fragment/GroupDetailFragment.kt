package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentDetailGroupBinding
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.entities.Group
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.adapter.CompanyAdapter
import com.example.businesswatcher.project.ui.adapter.DetailGroupSectorAdapter
import com.example.businesswatcher.project.ui.viewmodel.GroupDetailViewModel
import com.example.businesswatcher.project.utils.dialogBuilder
import com.example.businesswatcher.project.utils.setPhotoUrl
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailFragment : Fragment() {

    private var _bind: FragmentDetailGroupBinding? = null
    private val bind get() = _bind!!

    companion object {
        const val ID = "id"
    }

    private val viewModel: GroupDetailViewModel by viewModels()

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { id = it.getLong(ID).toString() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_group, container, false)
        (activity as MainActivity?)!!.setNavigationView(true)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.backBtn.setOnClickListener { findNavController().navigateUp() }

        viewModel.getGroup(id.toLong()).observe(viewLifecycleOwner) { descriptionBtn(it) }
        viewModel.name.observe(viewLifecycleOwner) { bind.name.text = it }
        viewModel.description.observe(viewLifecycleOwner) { bind.description.text = it }
        viewModel.logo.observe(viewLifecycleOwner) {
            Glide.with(this).load(setPhotoUrl(it)).circleCrop().into(bind.logo)
        }

        viewModel.rank.observe(viewLifecycleOwner) { bind.rank.text = getString(R.string.rank, it) }
        viewModel.turnover.observe(viewLifecycleOwner) {
            bind.turnover.text = getString(R.string.euro, it)
        }
        viewModel.numberOfCompanies.observe(viewLifecycleOwner) {
            bind.companiesNumber.text = getString(R.string.companies, it)
        }
        companies()
        businessSectors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)!!.setNavigationView(false)
        _bind = null
    }

    private fun companies() = viewModel.companies.observe(viewLifecycleOwner) {
        val companies: MutableList<Company> = mutableListOf()
        val businessSectors: MutableList<BusinessSector> = mutableListOf()

        for (businessSectorWithCompanies in it) {
            businessSectors.add(businessSectorWithCompanies.businessSector)
            companies.addAll(businessSectorWithCompanies.companies)
        }

        companies.sortedWith(
            compareBy<Company> { company -> company.businessSectorId }
                .thenByDescending { company -> company.turnover }
        )

        bind.companiesList.adapter = CompanyAdapter(companies, businessSectors)

        if (companies.isEmpty()) bind.emptyLayout.visibility = View.VISIBLE
        else bind.emptyLayout.visibility = View.GONE
    }

    private fun businessSectors() =
        viewModel.businessSectors(id.toLong()).observe(viewLifecycleOwner) {
            bind.businessSectorList.adapter = DetailGroupSectorAdapter(it)
        }

    private fun descriptionBtn(group: Group) =
        bind.descriptionButton.setOnClickListener { descriptionDialog(group) }

    private fun descriptionDialog(group: Group) {
        val customAlertDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_update_description, null, false)

        val descriptionTextField: TextInputLayout =
            customAlertDialogView.findViewById(R.id.description_layout)

        descriptionTextField.editText!!.setText(group.description)

        dialogBuilder(requireContext(), "Description").setView(customAlertDialogView)
            .setPositiveButton(getString(R.string.description_dialog_positive_btn)) { dialog, _ ->
                val description = descriptionTextField.editText!!.text.toString()
                if (description.isNotBlank()) updateDescription(description, group.id)
                dialog.dismiss()
            }.show()
    }

    private fun updateDescription(description: String, id: Long) =
        viewModel.updateDescription(description, id)
}