package com.example.customerfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.ProductItemDetailsBinding
import com.example.customerfirebase.model.ProductDetails
import com.google.android.material.textview.MaterialTextView


class ProductAdapter(
    private val listener: OnItemClickListener,
    val context: Context,
    val list: ArrayList<ProductDetails>,
) : RecyclerView.Adapter<ProductAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(private val binding: ProductItemDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal var mtvProductName: MaterialTextView

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val productDetails = list[position]
                        listener.onItemClick(productDetails)
                    }
                }
            }
            mtvProductName =
                itemView.findViewById(R.id.mtvProductName) // Initialize your All views prensent in list items
        }

        fun bind(position: Int) {
            binding.apply {
                mtvProductName.text = list[position].productName
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(productDetails: ProductDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ProductItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductDetails>() {
        override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails) =
            oldItem.productName == newItem.productName

        override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails) =
            oldItem == newItem
    }

}



