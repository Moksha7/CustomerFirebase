package com.example.customerfirebase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.example.customerfirebase.model.Product

@Database(entities = [Customer::class, CustomerData::class, Product::class],
    version = 1,
    exportSchema = false)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun getCustomerDao(): CustomerDao
}