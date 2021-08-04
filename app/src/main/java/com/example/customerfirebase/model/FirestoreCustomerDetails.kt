package com.example.customerfirebase.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize

data class FirestoreCustomerDetails(
    var customerId: Long = 0,
    var customerName: String = "",
    var customerAddress: String = "",
    var customerVillage: String = "",
    var customerCity: String = "",
    var customerLocation: String = "",
    var customerMobile: String = "",
) : Parcelable
