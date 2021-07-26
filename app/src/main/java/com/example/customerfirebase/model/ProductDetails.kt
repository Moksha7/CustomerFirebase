package com.example.customerfirebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetails(
    var productId: Long = 0,
    var productCategory: String = "",
    var productName: String = "",
    var productQuantity: String = "",
    var productPrice: String = "",
    var productTotal: String = "",
) : Parcelable
