package com.example.businesswatcher.project.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.FragmentGroupCreationBinding
import com.example.businesswatcher.project.ui.MainActivity
import com.example.businesswatcher.project.ui.viewmodel.GroupCreationViewModel
import com.example.businesswatcher.project.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupCreationFragment : Fragment() {

    private var _bind: FragmentGroupCreationBinding? = null
    private val bind get() = _bind!!

    private val viewModel: GroupCreationViewModel by viewModels()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var logo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) addPhoto(result.data?.data!!)
                else toast(requireContext(), getString(R.string.error_pick_file))
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group_creation, container, false)
        (activity as MainActivity?)!!.setNavigationView(true)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.backBtn.setOnClickListener { findNavController().navigateUp() }
        validationBtn()
        logoBtn()
        deleteLogoBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)!!.setNavigationView(false)
        _bind = null
    }

    private fun logoBtn() = bind.logoBtn.setOnClickListener {
        if (isFileAccessAllowed(requireContext())) resultLauncher.launch(pickPhoto())
        else askFileAccessPermission(requireActivity())
    }

    private fun addPhoto(uri: Uri) = viewModel.uploadImage(uri).observe(viewLifecycleOwner) {
        logo = it
        setLogoView(true)
    }

    private fun deleteLogoBtn() = bind.deleteBtn.setOnClickListener { setLogoView(false) }

    private fun setLogoView(isPhotoTaken: Boolean) {
        if (isPhotoTaken) {
            bind.logoBtn.visibility = View.GONE
            bind.deleteBtn.visibility = View.VISIBLE
        } else {
            bind.logoBtn.visibility = View.VISIBLE
            bind.deleteBtn.visibility = View.GONE
            logo = ""
        }
        Glide.with(this).load(setPhotoUrl(logo)).circleCrop().into(bind.logoView)
    }

    private fun validationBtn() = bind.validateBtn.setOnClickListener {
        viewModel.createGroup(
            bind.nameInput.text.toString(),
            logo,
            bind.descriptionInput.text.toString()
        ).observe(viewLifecycleOwner) {
            if (it == true) findNavController().navigateUp()
            else toast(requireContext(), getString(R.string.error_empty_fields))
        }
    }
}
