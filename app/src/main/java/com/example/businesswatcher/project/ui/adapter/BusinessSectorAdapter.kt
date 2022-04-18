package com.example.businesswatcher.project.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.ItemBusinessSectorBinding
import com.example.businesswatcher.project.domain.relations.BusinessSectorWithCompanies

class BusinessSectorAdapter(private val dataset: List<BusinessSectorWithCompanies>) :
    RecyclerView.Adapter<BusinessSectorAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataset.size

    class ViewHolder(val bind: ItemBusinessSectorBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemBusinessSectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder) {
            with(dataset[position]) {
                bind.color.setBackgroundColor(Color.parseColor(businessSector.color))
                bind.name.text = businessSector.name
                bind.turnover.text = itemView.context.getString(
                    R.string.sector_turnover, companies.sumOf { it.turnover }.toString()
                )
                bind.companies.text =
                    itemView.context.getString(R.string.sector_companies, companies.size.toString())
            }
        }
}