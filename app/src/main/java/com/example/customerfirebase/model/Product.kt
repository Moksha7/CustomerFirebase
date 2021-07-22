package com.example.customerfirebase.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.customerfirebase.utils.Constant.PRODUCT_CATEGORY
import com.example.customerfirebase.utils.Constant.PRODUCT_NAME
import com.example.customerfirebase.utils.Constant.PRODUCT_PRICE
import com.example.customerfirebase.utils.Constant.PRODUCT_QUANTITY
import com.example.customerfirebase.utils.Constant.PRODUCT_TOTAL
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
@kotlinx.parcelize.Parcelize
@Entity(tableName = "Product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var productId: Int = 0,
    @ColumnInfo(name = PRODUCT_CATEGORY)
    var productCategory: String = "",
    @ColumnInfo(name = PRODUCT_NAME)
    var productName: String = "",
    @ColumnInfo(name = PRODUCT_QUANTITY)
    var productQuantity: String = "",
    @ColumnInfo(name = PRODUCT_PRICE)
    var productPrice: String = "",
    @ColumnInfo(name = PRODUCT_TOTAL)
    var productTotal: String = "",
) : Parcelable
