package com.example.creativegalileo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.creativegalileo.databinding.CustomerListItemBinding
import com.example.creativegalileo.models.Customer

class CustomerListPagingAdapter :
    PagingDataAdapter<Customer, CustomerListPagingAdapter.CustomerViewHolder>(COMPARATOR) {

        class CustomerViewHolder(var binding: CustomerListItemBinding)
            : RecyclerView.ViewHolder(binding.root)

        override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
            val item = getItem(position)
            holder.binding.custId.text = item?.cgId.toString()
            holder.binding.custName.text = item?.name
            holder.binding.custMobile.text = ""+item?.dialCode+item?.mobile
            holder.binding.custEmail.text = item?.email

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CustomerListItemBinding.inflate(inflater, parent, false)
            return CustomerViewHolder(binding)
        }

        companion object {
            private val COMPARATOR = object : DiffUtil.ItemCallback<Customer>() {
                override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                    return oldItem == newItem
                }
            }
        }
}