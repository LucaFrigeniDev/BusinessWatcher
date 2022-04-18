package com.example.businesswatcher.project.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.ItemGroupBinding
import com.example.businesswatcher.project.domain.relations.GroupWithCompanies
import com.example.businesswatcher.project.utils.groupDetail
import com.example.businesswatcher.project.utils.setPhotoUrl

class GroupAdapter(private val dataset: List<GroupWithCompanies>) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataset.size

    class ViewHolder(val bind: ItemGroupBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder) {
            with(dataset[position]) {

                Glide.with(itemView.context)
                    .load(setPhotoUrl(group.logo))
                    .circleCrop()
                    .into(bind.logo)

                bind.name.text = group.name
                bind.companies.text =
                    itemView.context.getString(R.string.group_companies, companies.size.toString())
                bind.turnover.text = itemView.context.getString(
                    R.string.euro_turnover, companies.sumOf { it.turnover }.toString()
                )

                itemView.setOnClickListener { groupDetail(group.id) }
            }
        }
}