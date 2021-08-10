package com.example.customerfirebase.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.customerfirebase.model.ProductDetails

class ProductDetailsViewModel(productDetails: ProductDetails?) : BaseObservable() {

    @Bindable
    var productName: String = productDetails?.productName ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productCategory: String = productDetails?.productCategory ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productQuantity: String = productDetails?.productQuantity ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productPrice: String = productDetails?.productPrice ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productTotal: String = productDetails?.productTotal ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productUrl: String = productDetails?.productImageUrl ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productId: Long = productDetails?.productId ?: 0
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerId: String = productDetails?.customerId ?: ""
        set(value) {
            field = value
            notifyChange()
        }


    @Bindable
    var productInsertDate: String = productDetails?.productOrderDate ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var productDeliveredDate: String = productDetails?.productDeliveredDate ?: ""
        set(value) {
            field = value
            notifyChange()
        }


}