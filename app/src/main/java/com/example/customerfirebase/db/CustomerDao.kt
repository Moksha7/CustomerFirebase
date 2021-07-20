package com.example.customerfirebase.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customerData: CustomerData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustId(customer: Customer)

    @Query("SELECT * FROM customer WHERE id=:id")
    fun getCustomerById(id: String): Customer?

}