package com.example.customerfirebase.viewmodel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.customerfirebase.db.DbRepository
import com.example.customerfirebase.model.*
import com.example.customerfirebase.ui.fragment.CustomerRegistrationFragmentDirections
import com.example.customerfirebase.ui.fragment.LoginFragmentDirections
import com.example.customerfirebase.ui.fragment.ProductInsertFragmentDirections
import com.example.customerfirebase.utils.Constant.PRODUCT
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

var maxid = 2000
@HiltViewModel
class FirebaseViewModel @Inject
constructor(
    @ApplicationContext private val context: Context,
    private val dbRepository: DbRepository,
    private val firebaseFireStore: FirebaseFirestore,

) : ViewModel() {
    val TAG = "LoginFragment"
    var productList = MutableLiveData<ArrayList<ProductDetails>>()
    var newProductList = arrayListOf<ProductDetails>()

    fun insertCustDataIntoRoomDB(id: String, name: String, pass: String) {
        val customerData = CustomerData(id, name, pass)
        dbRepository.insertCustomer(customerData)
        Log.d(TAG, "Cust data added")
    }

    fun insertCustIdIntoRoomDB(id: String) {
        val cId = Customer(id)
        dbRepository.insertCustId(cId)
        Log.d(TAG, "cust id added")
    }

    fun checkCustIdIntoRoomDB(id: String, name: String, pass: String) {
        val cId = dbRepository.getCustomerById(id)
        if (cId != null) {
            insertCustDataIntoRoomDB(id, name, pass)
            //insertCustDataIntoFireStore(id, name, pass)
            Log.d(TAG, "User is valid")
        } else {
            Log.d(TAG, "User is not valid")
        }
    }

    fun insertCustDataIntoFireStore(id: String, name: String, pass: String) {
        val customerData = hashMapOf(
            "cid" to id,
            "cname" to name,
            "cpass" to pass
        )

        firebaseFireStore.collection("customerData").document(id)
            .set(customerData)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot custData successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing custData document",
                    e)
            }
    }

    fun autoIncrementCustId() {
        firebaseFireStore.collection("customerDetails").get().addOnSuccessListener { document ->
            if (document != null) {
                for (changes in document.documentChanges) {
                    val doc = changes.document
                    maxid = doc.id.toInt()
                }
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }
    }

    fun insertCustomerDetailsIntoFireStore(
        customerName: String,
        customerAddress: String,
        customerVillage: String,
        customerCity: String,
        customerLocation: String,
        customerMobile: String,
        navController: NavController,
    ) {
        autoIncrementCustId()
        val customerDetail = CustomerDetails(maxid,
            customerName,
            customerAddress,
            customerVillage,
            customerCity,
            customerLocation,
            customerMobile)
        //saveCustId(maxid.toString())
        // loadCustId()
        firebaseFireStore.collection("customerDetails").document(maxid.toString())
            .set(customerDetail)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot CustomerDetails successfully written!")
                maxid = maxid + 1
                val action =
                    CustomerRegistrationFragmentDirections.actionCustomerRegistrationFragmentToCategoryFragment()
                navController.navigate(action)
                //saveCustId(maxid.toString())
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing CustomerDetails document",
                    e)
            }

    }


    fun loadCustId() {
        val location = "customerId"
        val prefs = context.getSharedPreferences("CustomerId",
            AppCompatActivity.MODE_PRIVATE)
        val city = prefs?.getString(location, "").toString()
        //maxid = city.toInt()
    }

    fun saveCustId(cid: String?) {
        val location = "customerId"
        val prefs = context.getSharedPreferences("CustomerId",
            AppCompatActivity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(location, cid)
        editor.apply()
    }


    fun insertCustIdIntoFireStore(id: String) {
        val customerId = hashMapOf(
            "cid" to id
        )

        firebaseFireStore.collection("customer").document(id)
            .set(customerId)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot custId successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing custId document",
                    e)
            }
    }

    fun checkCustIdIntoFireStore(id: String, name: String, pass: String) {
        val custIdRef = firebaseFireStore.collection("customer")
        custIdRef.document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "Document Snapshot Data: ${document.data}")
                val ccid = document.getString("cid")
                if (ccid == id) {
                    insertCustDataIntoFireStore(id, name, pass)
                    Log.d(TAG, "User Valid")
                } else {
                    Log.d(TAG, "User not Valid")
                }
            } else {
                Log.d(TAG, "No such Document Snapshot")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }
    }

    fun checkLoginDataIntoFireStore(id: String, pass: String, navController: NavController) {
        val custLoginRef = firebaseFireStore.collection("customerData")

        custLoginRef.document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "Document Snapshot Data: ${document.data}")
                val ccid = document.getString("cid")
                val ccpass = document.getString("cpass")
                if (ccid == id && ccpass == pass) {
                    Log.d(TAG, "User Valid")
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToCustomerRegistrationFragment()
                    navController.navigate(action)

                } else {
                    Log.d(TAG, "User not Valid")
                }
            } else {
                Log.d(TAG, "No such Document Snapshot")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }


    }


    fun loadProductDetailsFromCategory(category: String) {
        newProductList.clear()
        val productRef = firebaseFireStore.collection(PRODUCT)
        productRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(TAG, "Document Product Snapshot Data: ")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if (category == document.getString("productCategory")) {
                        val productName = document.getString("productName")
                        val productQuantity = document.getString("productQuantity")
                        val productPrice = document.getString("productPrice")
                        val productTotal = document.getString("productTotal")
                        val productCategory = document.getString("productCategory")
                        val product = document.toObject(ProductDetails::class.java)
                        newProductList.add(product)
                        productList.value = newProductList
                        Log.d(TAG, "Product Name : " + productName)
                        Log.d(TAG, "Product Quantity: " + productQuantity)
                        Log.d(TAG, "Product Price: " + productPrice)
                        Log.d(TAG, "Product Total: " + productTotal)
                        Log.d(TAG, "Product Category: " + productCategory)
                    }
                }
            }
            Log.d(TAG, "Product Details Document Snapshot")


        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }

        Log.d(TAG, "product List : " + productList.value)


    }


    fun addProductDetails(
        name: String,
        category: String,
        quantity: String,
        price: String,
        total: String,
        navController: NavController,
    ) {
        val product = Product()
        product.productName = name
        product.productTotal = total
        product.productQuantity = quantity
        product.productCategory = category
        product.productPrice = price

        dbRepository.insertProduct(product)
        firebaseFireStore.collection(PRODUCT).document()
            .set(product)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot Product Details successfully written!")
                val action =
                    ProductInsertFragmentDirections.actionProductInsertFragmentToProductFragment()
                navController.navigate(action)
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing ProductDetails document",
                    e)
            }


    }

}





