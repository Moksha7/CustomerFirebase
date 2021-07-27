package com.example.customerfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.CustomerItemBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.google.android.material.textview.MaterialTextView


class CustomerAdapter(
    private val listener: OnClickListener,
    val context: Context,
    val list: ArrayList<FirestoreCustomerDetails>,
) : RecyclerView.Adapter<CustomerAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(private val binding: CustomerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal var mtvCustomerName: MaterialTextView
        internal var mtvCustomerAddress: MaterialTextView
        internal var mtvCustomerDistrict: MaterialTextView

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val customerDetails = list[position]
                        listener.onClick(customerDetails)
                    }
                }
            }
            mtvCustomerName =
                itemView.findViewById(R.id.mtvCustomerName)
            mtvCustomerAddress =
                itemView.findViewById(R.id.mtvCustomerAddress)// Initialize your All views prensent in list items
            mtvCustomerDistrict =
                itemView.findViewById(R.id.mtvCustomerCity)// Initialize your All views prensent in list items
        }

        fun bind(position: Int) {
            binding.apply {
                mtvCustomerName.text = list[position].customerName
                mtvCustomerAddress.text = list[position].customerAddress
                mtvCustomerDistrict.text = list[position].customerCity
            }
        }

    }

    interface OnClickListener {
        fun onClick(customerDetails: FirestoreCustomerDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            CustomerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class DiffCallback : DiffUtil.ItemCallback<FirestoreCustomerDetails>() {
        override fun areItemsTheSame(
            oldItem: FirestoreCustomerDetails,
            newItem: FirestoreCustomerDetails,
        ) =
            oldItem.customerName == newItem.customerName

        override fun areContentsTheSame(
            oldItem: FirestoreCustomerDetails,
            newItem: FirestoreCustomerDetails,
        ) =
            oldItem == newItem
    }

}



