package com.example.customerfirebase.db

import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.example.customerfirebase.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbRepository @Inject constructor(
    private val customerDao: CustomerDao,
) : DefaultDbRepo {

    override fun insertCustomer(customerData: CustomerData) {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.insertCustomer(customerData)
        }
    }

    override fun getCustomerById(id: String) = customerDao.getCustomerById(id)

    override fun insertCustId(customer: Customer) {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.insertCustId(customer)
        }
    }

    override fun insertProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.insertProduct(product)
        }
    }

    override fun getProductByCategory(category: String) = customerDao.getProductByCategory(category)


}