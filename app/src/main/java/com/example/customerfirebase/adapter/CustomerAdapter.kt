package com.example.customerfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.CustomerItemBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.google.android.material.button.MaterialButton
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
        internal var mtvCustomerButton: MaterialButton

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val customerDetails = list[position]
                        listener.onClick(customerDetails)
                    }
                }
                btnCustomerItemDisplay.setOnClickListener {
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
            mtvCustomerButton = itemView.findViewById(R.id.btn_customer_item_display)
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
        holder.mtvCustomerName.text = list[position].customerName
        holder.mtvCustomerAddress.text = list[position].customerAddress
        holder.mtvCustomerDistrict.text = list[position].customerCity
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    class DiffCallback : DiffUtil.ItemCallback<FirestoreCustomerDetails>() {
        override fun areItemsTheSame(
            oldItem: FirestoreCustomerDetails,
            newItem: FirestoreCustomerDetails,
        ) =
            oldItem.customerId == newItem.customerId

        override fun areContentsTheSame(
            oldItem: FirestoreCustomerDetails,
            newItem: FirestoreCustomerDetails,
        ) =
            oldItem == newItem
    }

}



