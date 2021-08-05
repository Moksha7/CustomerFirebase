package com.example.customerfirebase.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerfirebase.R
import com.example.customerfirebase.model.OrderDetails
import com.google.android.material.textview.MaterialTextView
import java.util.*

class OrderAdapter(
    private val listener: OrderAdapter.OnClickListener,
    val context: Context,
    val list: ArrayList<OrderDetails>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mtvProductName: MaterialTextView =
            itemView.findViewById(R.id.mtvProductName) // Initialize your All views prensent in list items
        var mtvProductQuantity: MaterialTextView =
            itemView.findViewById(R.id.mtvProductQuantity)
        var mtvProductPrice: MaterialTextView =
            itemView.findViewById(R.id.mtvProductPrice)
        var mtvProductTotal: MaterialTextView =
            itemView.findViewById(R.id.mtvProductTotal)
        var mtvProductOrderDate: MaterialTextView =
            itemView.findViewById(R.id.mtvProductOrderDate)
        var imageProduct: ImageView = itemView.findViewById(R.id.image_product)
        var cardProduct: CardView = itemView.findViewById(R.id.card_view_customer)

        init {
            cardProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val orderDetails = list[position]
                    listener.onClick(orderDetails)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            mtvProductName.text = recyclerViewModel.productName
            mtvProductQuantity.text = "Quantity : " + recyclerViewModel.productQuantity
            mtvProductPrice.text = "Price : " + recyclerViewModel.productPrice
            mtvProductTotal.text = "Total : " + recyclerViewModel.productTotal
            mtvProductOrderDate.text = recyclerViewModel.productOrderDate
            val bmp = stringToBitMap(list[position].productImageUrl)
            imageProduct.setImageBitmap(bmp)
            Glide.with(context).load(list[position].productImageUrl).into(imageProduct)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val preferences: SharedPreferences = context.getSharedPreferences("saveSpanCount",
            Context.MODE_PRIVATE)
        val spanCount = preferences.getInt("spanCount", 1)
        if (spanCount == 2) {
            val v: View =
                LayoutInflater.from(context).inflate(R.layout.order_item_grid, parent, false)
            return View1ViewHolder(v)
        } else {
            val vv: View =
                LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false)
            return View1ViewHolder(vv)
        }
    }

    override fun getItemCount(): Int {
        return list.size
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
        fun onClick(orderDetails: OrderDetails)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(position)
    }


}

