package com.example.customerfirebase.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
@kotlinx.parcelize.Parcelize
@Entity
data class Customer(
    @PrimaryKey
    var id: String = "",
) : Parcelable
