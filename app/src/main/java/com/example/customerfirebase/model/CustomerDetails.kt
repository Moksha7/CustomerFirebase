package com.example.customerfirebase.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "customerdetails")
data class CustomerDetails(
    @PrimaryKey(autoGenerate = true)
    var customerId: Int = 0,
    var customerName: String = "",
    var customerAddress: String = "",
    var customerVillage: String = "",
    var customerCity: String = "",
    var customerLocation: String = "",
    var customerMobile: String = "",
) : Parcelable
