package com.example.customerfirebase.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.customerfirebase.model.FirestoreCustomerDetails

class CustomerRegisterViewModel(firebaseCustomerDetails: FirestoreCustomerDetails?) :
    BaseObservable() {

    @Bindable
    var customerId: Long = firebaseCustomerDetails?.customerId ?: 0
        set(value) {
            field = value
            notifyChange()
        }


    @Bindable
    var customerName: String = firebaseCustomerDetails?.customerName ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerAddress: String = firebaseCustomerDetails?.customerAddress ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerVillage: String = firebaseCustomerDetails?.customerVillage ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerDistrict: String = firebaseCustomerDetails?.customerCity ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerLocation: String = firebaseCustomerDetails?.customerLocation ?: ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerMobile: String = firebaseCustomerDetails?.customerMobile ?: ""
        set(value) {
            field = value
            notifyChange()
        }
}