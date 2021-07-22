package com.example.customerfirebase.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.example.customerfirebase.model.Product

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customerData: CustomerData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM Product WHERE Product_Category=:category")
    fun getProductByCategory(category: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustId(customer: Customer)

    @Query("SELECT * FROM customer WHERE id=:id")
    fun getCustomerById(id: String): Customer?

}