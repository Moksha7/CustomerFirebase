package com.example.customerfirebase.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "customerdata")
data class CustomerData(
    @PrimaryKey
    var ccid: String = "",

    val cid: String = "",

    var cname: String = "",
    var cpass: String = "",
) : Parcelable

