package com.example.customerfirebase.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.customerfirebase.R
import com.google.android.material.textview.MaterialTextView


class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var productName: MaterialTextView

    init {
        productName = view.findViewById(R.id.mtvProductName)
    }
}
