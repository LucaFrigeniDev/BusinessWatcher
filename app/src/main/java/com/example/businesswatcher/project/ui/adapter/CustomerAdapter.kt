package com.example.businesswatcher.project.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businesswatcher.databinding.ItemCustomerBinding
import com.example.businesswatcher.project.domain.other.Customer

class CustomerAdapter(
    private val dataset: List<Customer>,
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataset.size

    class ViewHolder(val bind: ItemCustomerBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(holder) {
            with(dataset[position]) {
                bind.name.text = name
                bind.description.text = description
                bind.updateButton.setOnClickListener {}
            }
        }
}