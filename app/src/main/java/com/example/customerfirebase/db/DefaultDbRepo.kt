package com.example.customerfirebase.db

import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.example.customerfirebase.model.Product

interface DefaultDbRepo {

    fun insertCustomer(customerData: CustomerData)

    fun getCustomerById(id: String): Customer?

    fun insertCustId(customer: Customer)

    fun insertProduct(product: Product)

    fun getProductByCategory(category: String): Product?
}
