package com.example.customerfirebase.model

import android.annotation.SuppressLint
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
import java.util.*

@IgnoreExtraProperties
@kotlinx.parcelize.Parcelize
@Entity(tableName = "Remainder")
data class Remainder(
    @PrimaryKey(autoGenerate = true)
    var remainderId: Int = 0,
    @ColumnInfo(name = "orderId")
    var orderId: String = "",
    @ColumnInfo(name = "productId")
    var productId: String = "",
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
    @ColumnInfo(name = "customerId")
    var customerId: String = "",
    @ColumnInfo(name = "customerName")
    var customerName: String = "",
    @ColumnInfo(name = "productOrderDate")
    var productOrderDate: String = "",
    @ColumnInfo(name = "productDeliveredDate")
    var productDeliveredDate: String = "",
    @ColumnInfo(name = "productImageUrl")
    var productImageUrl: String = "",
    @ColumnInfo(name = "remainderIdentifier")
    var reminderIndentifier: String = UUID.randomUUID().toString(),
    @SuppressLint("SimpleDateFormat")
    @ColumnInfo(name = "remainderDate")
    var remainderDate: String = "",
    @ColumnInfo(name = "remainderTime")
    var remainderTime: String = "",
    @ColumnInfo(name = "remainderRepeat")
    var remainderRepeat: Boolean = true,
    @ColumnInfo(name = "remainderRepeatValue")
    var remainderRepeatValue: Int = 1,
    @ColumnInfo(name = "remainderRepeatUnit")
    var remainderRepeatUnit: String = "Day",
    @ColumnInfo(name = "remainderActive")
    var remainderIsActive: Boolean = true,
) : Parcelable
