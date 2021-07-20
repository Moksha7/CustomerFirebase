package com.example.customerfirebase.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class LoginViewModel : BaseObservable() {

    @Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var pass: String = ""
        set(value) {
            field = value
            notifyChange()
        }

}
