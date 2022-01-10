package com.example.businesswatcher.project.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.businesswatcher.R
import com.example.businesswatcher.project.models.Company

class CompanyAdapter(private val dataset: List<Company>) :
    RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder =
        CompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false)
        )

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int = dataset.size

    class CompanyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.name)


    }
}