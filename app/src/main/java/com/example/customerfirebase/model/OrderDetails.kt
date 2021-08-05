package com.example.customerfirebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetails(
    var orderId: Long = 0,
    var productId: String = "",
    var productCategory: String = "",
    var productName: String = "",
    var productQuantity: String = "",
    var productPrice: String = "",
    var productTotal: String = "",
    var customerId: String = "",
    var productInsertDate: String = "",
    var productDeliveredDate: String = "",
    var productImageUrl: String = "",
    var productOrderDate: String = "",
) : Parcelable
