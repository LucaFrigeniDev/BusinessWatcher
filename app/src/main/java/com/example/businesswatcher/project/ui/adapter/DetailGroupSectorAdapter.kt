package com.example.businesswatcher.project.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businesswatcher.R
import com.example.businesswatcher.databinding.ItemGroupDetailSectorBinding
import com.example.businesswatcher.project.domain.other.GroupBusinessSector

class DetailGroupSectorAdapter(
    private val dataset: List<GroupBusinessSector>
) : RecyclerView.Adapter<DetailGroupSectorAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataset.size

    class ViewHolder(val bind: ItemGroupDetailSectorBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemGroupDetailSectorBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder) {
            with(dataset[position]) {
                bind.sectorCard.setCardBackgroundColor(Color.parseColor(color))
                bind.businessSectorName.text = businessSector
                bind.turnover.text = itemView.context.getString(R.string.euro, turnover)
                bind.companies.text = itemView.context.getString(R.string.companies, companies)
                bind.share.text = itemView.context.getString(R.string.share, share)
                bind.rank.text = rank
            }
        }
}