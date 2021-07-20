package com.example.customerfirebase

import android.app.Application
import android.content.Context
import com.example.customerfirebase.db.CustomerDao
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CustomerApplication : Application() {

    @Inject
    lateinit var customerDao: CustomerDao


    companion object {
        lateinit var instance: CustomerApplication
            private set
        lateinit var appContext: Context
        lateinit var customerDaoo: CustomerDao
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this
        customerDaoo = customerDao
        FirebaseApp.initializeApp(this)

    }
}