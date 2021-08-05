package com.example.customerfirebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemainderDetails(
    var remainderId: Long = 0,
    var orderId: String = "",
    var productId: String = "",
    var productCategory: String = "",
    var productName: String = "",
    var productQuantity: String = "",
    var productPrice: String = "",
    var productTotal: String = "",
    var customerId: String = "",
    var customerName: String = "",
    var productOrderDate: String = "",
    var productDeliveredDate: String = "",
    var productImageUrl: String = "",
    var reminderIndentifier: String = "",
    var remainderDate: String = "",
    var remainderTime: String = "",
    var remainderRepeat: Boolean = true,
    var remainderRepeatValue: Int = 1,
    var remainderRepeatUnit: String = "",
    var remainderIsActive: Boolean = true,
) : Parcelable
