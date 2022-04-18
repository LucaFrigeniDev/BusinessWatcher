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
import com.example.businesswatcher.databinding.FragmentDetailCompanyBinding
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.domain.other.Customer
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.adapter.CustomerAdapter
import com.example.businesswatcher.project.ui.viewmodel.CompanyDetailViewModel
import com.example.businesswatcher.project.utils.dialogBuilder
import com.example.businesswatcher.project.utils.setPhotoUrl
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyDetailFragment : Fragment() {

    private var _bind: FragmentDetailCompanyBinding? = null
    private val bind get() = _bind!!

    companion object {
        const val ID = "id"
    }

    private val viewModel: CompanyDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { viewModel.getCompany(it.getLong(ID)) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind = DataBindingUtil
            .inflate(inflater, R.layout.fragment_detail_company, container, false)
        (activity as MainActivity?)!!.setNavigationView(true)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.backBtn.setOnClickListener { findNavController().navigateUp() }

        viewModel.company.observe(viewLifecycleOwner) { company ->
            if (company != null) {
                company(company)
                customers(company)
                bind.descriptionButton.setOnClickListener { descriptionDialog(company.id) }
                bind.customersBtn.setOnClickListener {
                    customerDialog(company.customers, company.id)
                }
            }
        }
        group()
        businessSector()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)!!.setNavigationView(false)
        _bind = null
    }

    private fun group() =
        viewModel.groupName.observe(viewLifecycleOwner) { bind.groupName.text = it }

    private fun businessSector() =
        viewModel.businessSectorName.observe(viewLifecycleOwner) { bind.businessSector.text = it }

    private fun customers(company: Company) {
        bind.customersList.adapter = CustomerAdapter(company.customers)
        if (company.customers.isEmpty()) bind.emptyLayout.visibility = View.VISIBLE
        else bind.emptyLayout.visibility = View.GONE
    }

    private fun company(company: Company) {
        Glide.with(this).load(setPhotoUrl(company.logo)).circleCrop().into(bind.logo)
        bind.companyName.text = company.name
        bind.description.text = company.description
        bind.turnover.text = getString(R.string.euro, company.turnover.toString())
        bind.address.text =
            getString(R.string.location, company.address, company.city, company.postalCode)
    }

    private fun descriptionDialog(id: Long) {
        val customAlertDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_update_description, null, false)

        val descriptionTextField: TextInputLayout =
            customAlertDialogView.findViewById(R.id.description_layout)

        viewModel.company.observe(viewLifecycleOwner) {
            descriptionTextField.editText!!.setText(it.description)
        }

        dialogBuilder(requireContext(), "Description").setView(customAlertDialogView)
            .setPositiveButton(getString(R.string.description_dialog_positive_btn)) { dialog, _ ->
                val description = descriptionTextField.editText!!.text.toString()
                if (description.isNotBlank()) updateDescription(description, id)
                dialog.dismiss()
            }.show()
    }

    private fun updateDescription(description: String, id: Long) =
        viewModel.updateDescription(description, id)

    private fun customerDialog(customers: List<Customer>, id: Long) {
        val customAlertDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_customer, null, false)

        val nameTextField: TextInputLayout = customAlertDialogView.findViewById(R.id.name_layout)

        val descriptionTextField: TextInputLayout =
            customAlertDialogView.findViewById(R.id.description_layout)

        dialogBuilder(requireContext(), "Customer").setView(customAlertDialogView)
            .setPositiveButton(getString(R.string.add_sector_dialog_positive_btn)) { dialog, _ ->
                val name = nameTextField.editText!!.text.toString()
                val description = descriptionTextField.editText!!.text.toString()
                if (name.isNotBlank() && description.isNotBlank()) {
                    val customer = Customer(name, description)
                    addCustomer(customers, customer, id)
                }
                dialog.dismiss()
            }.show()
    }

    private fun addCustomer(customers: List<Customer>, customer: Customer, id: Long) =
        viewModel.addCustomer(customers, customer, id)
}