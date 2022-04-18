package com.example.businesswatcher.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentListGroupBinding
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.adapter.GroupAdapter
import com.example.businesswatcher.project.ui.viewmodel.GroupListViewModel
import com.example.businesswatcher.project.utils.displayFragment
import com.example.businesswatcher.project.utils.groupsToCompanies
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListGroupFragment : Fragment() {
    private var _bind: FragmentListGroupBinding? = null
    private val bind get() = _bind!!

    private val viewModel: GroupListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        displayFragment = "ListGroupFragment"
        _bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_group, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter()
        filter()
        clearFilter()
        switchBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    private fun adapter() = viewModel.getGroups().observe(viewLifecycleOwner) {
        bind.recyclerView.adapter = GroupAdapter(it)
        if (it.isEmpty()) bind.emptyLayout.visibility = View.VISIBLE
        else bind.emptyLayout.visibility = View.GONE
    }

    private fun filter() = (activity as MainActivity).filterBtn.setOnClickListener {
        (activity as MainActivity).setFilters()
        (activity as MainActivity).hideBottomSheet()
        adapter()
    }

    private fun clearFilter() = (activity as MainActivity).clearBtn.setOnClickListener {
        (activity as MainActivity).clear()
        adapter()
    }

    private fun switchBtn() = bind.changeButton.setOnClickListener { groupsToCompanies() }
}