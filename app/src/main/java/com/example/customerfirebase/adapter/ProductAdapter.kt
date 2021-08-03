package com.example.customerfirebase.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.ProductItemDetailsBinding
import com.example.customerfirebase.model.ProductDetails
import com.google.android.material.textview.MaterialTextView
import java.io.ByteArrayOutputStream
import java.util.*


class ProductAdapter(
    private val listener: OnClickListener,
    val context: Context,
    val list: ArrayList<ProductDetails>,
) : RecyclerView.Adapter<ProductAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(private val binding: ProductItemDetailsBinding?) :
        RecyclerView.ViewHolder(binding?.root!!) {

        internal var mtvProductName: MaterialTextView
        internal var imageProduct: ImageView

        init {
            binding?.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val productDetails = list[position]
                        listener.onClick(productDetails)
                    }
                }
                cardViewCustomer.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val productDetails = list[position]
                        listener.onClick(productDetails)
                    }
                }
            }
            mtvProductName =
                itemView.findViewById(R.id.mtvProductName) // Initialize your All views prensent in list items
            imageProduct =
                itemView.findViewById(R.id.image_product)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(position: Int) {
            binding.apply {
//                val newHeight : Int = 80
//                imageProduct.layoutParams.height = newHeight
                val bmp = stringToBitMap(list[position].productImageUrl)
                mtvProductName.text = list[position].productName
                imageProduct.setImageBitmap(bmp)
                Glide.with(context).load(list[position].productImageUrl).into(imageProduct)
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToBitMap(string: String): Bitmap? {
        return try {
            val imageBytes = Base64.getDecoder().decode(string)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            scaleToFitHeight(image, 80)
        } catch (e: Exception) {
            Log.d("Image", e.message.toString())
            return null
        }
    }

    fun scaleToFitHeight(b: Bitmap, height: Int): Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, true)
    }

    interface OnClickListener {
        fun onClick(productDetails: ProductDetails)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val preferences: SharedPreferences =
            context.getSharedPreferences("saveSpanCount", MODE_PRIVATE)
        val spanCount = preferences.getInt("spanCount", 1)
        if (spanCount == 2) {
            val binding =
                ProductItemDetailsBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            //changeView(binding)

            return TasksViewHolder(binding)
        } else {
            val binding =
                ProductItemDetailsBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            //changeView(binding)
            return TasksViewHolder(binding)
        }


    }


    /*fun changeView(binding: ProductItemDetailsBinding) {
        val preferences: SharedPreferences = context.getSharedPreferences("saveSpanCount", MODE_PRIVATE)
        val spanCount = preferences.getInt("spanCount", 1)
        if (spanCount == 2) {
            binding.cardViewCustomer.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))
        } else {
            binding.cardViewCustomer.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
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



