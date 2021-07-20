package com.example.customerfirebase.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class RegisterViewModel : BaseObservable() {

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

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