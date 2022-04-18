package com.example.businesswatcher.project.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.ItemCompanyBinding
import com.example.businesswatcher.project.domain.entities.BusinessSector
import com.example.businesswatcher.project.domain.entities.Company
import com.example.businesswatcher.project.utils.listToCompanyDetail
import com.example.businesswatcher.project.utils.setPhotoUrl

class CompanyAdapter(
    private val dataset: List<Company>,
    private val businessSectors: List<BusinessSector>
) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataset.size

    class ViewHolder(val bind: ItemCompanyBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder) {
            with(dataset[position]) {

                Glide.with(itemView.context)
                    .load(setPhotoUrl(logo))
                    .circleCrop()
                    .into(bind.logo)

                for (businessSector in businessSectors)
                    if (businessSector.id == businessSectorId)
                        bind.businessSector.setBackgroundColor(Color.parseColor(businessSector.color))

                bind.name.text = name
                bind.address.text = city
                bind.turnover.text =
                    itemView.context.getString(R.string.euro_turnover, turnover.toString())

                holder.itemView.setOnClickListener { listToCompanyDetail(id) }
            }
        }
}