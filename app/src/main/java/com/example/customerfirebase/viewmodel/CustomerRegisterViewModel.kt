package com.example.customerfirebase.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class CustomerRegisterViewModel : BaseObservable() {

    @Bindable
    var customerName: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerAddress: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerVillage: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerDistrict: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerLocation: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var customerMobile: String = ""
        set(value) {
            field = value
            notifyChange()
        }
}