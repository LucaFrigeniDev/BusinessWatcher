package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.businesswatcher.R
import com.example.businesswatcher.project.App
import com.example.businesswatcher.project.models.Company
import com.example.businesswatcher.project.ui.activity.MainActivity
import com.example.businesswatcher.project.viewmodel.CompanyViewModel
import com.example.businesswatcher.project.viewmodel.CompanyViewModelFactory

class ListFragment : Fragment() {

    private val companyViewModel: CompanyViewModel by viewModels {
        CompanyViewModelFactory((this.requireActivity().application as App).companyRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyViewModel.companyList.observe(viewLifecycleOwner, this::setList)
    }

    private fun setList(list: List<Company>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.list)
        recyclerView?.adapter = CompanyAdapter(list)
    }
}