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
import com.example.customerfirebase.utils.Constant.CUSTOMER_DETAILS_REF
import com.example.customerfirebase.utils.Constant.PRODUCT
import com.example.customerfirebase.utils.Constant.REMAINDER
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

var maxid = 0
var pid = 0
var rid = 0
@HiltViewModel
class FirebaseViewModel @Inject
constructor(
    @ApplicationContext private val context: Context,
    private val dbRepository: DbRepository,
    private val firebaseFireStore: FirebaseFirestore,

) : ViewModel() {
    val TAG = "LoginFragment"
    var productList = MutableLiveData<ArrayList<ProductDetails>>()


    var customerList = MutableLiveData<ArrayList<FirestoreCustomerDetails>>()



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

    fun autoIncrementCustId(
        customerName: String,
        customerAddress: String,
        customerVillage: String,
        customerCity: String,
        customerLocation: String,
        customerMobile: String,
        navController: NavController,
    ) {
        firebaseFireStore.collection("customerDetails")
            .orderBy("customerId", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var id = document.id
                    maxid += id.toInt()
                    insertCustomerDetailsIntoFireStore(maxid + 1,
                        customerName,
                        customerAddress,
                        customerVillage,
                        customerCity,
                        customerLocation,
                        customerMobile,
                        navController)
                }
            }
    }


    fun insertCustomerDetailsIntoFireStore(
        maxid: Int,
        customerName: String,
        customerAddress: String,
        customerVillage: String,
        customerCity: String,
        customerLocation: String,
        customerMobile: String,
        navController: NavController,
    ) {
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

                val action =
                    CustomerRegistrationFragmentDirections.actionCustomerRegistrationFragmentToCustomerDashboardFragment()
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
                val ccname = document.getString("cname")
                if (ccid == id && ccpass == pass) {
                    Log.d(TAG, "User Valid")
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToCustomerDashboardFragment(
                            ccname)
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

    fun loadCustomerFromFirestore() {
        val customerRef = firebaseFireStore.collection(CUSTOMER_DETAILS_REF)
        val newCustomerList = arrayListOf<FirestoreCustomerDetails>()
        customerRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(TAG, "Document Product Snapshot Data: ")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val customerDetails = document.toObject(FirestoreCustomerDetails::class.java)
                    newCustomerList.add(customerDetails)
                    customerList.value = newCustomerList
                }
            }
            Log.d(TAG, "customer Details Document Snapshot")
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }
        Log.d(TAG, "customer List : " + customerList.value)

    }


    fun loadProductDetailsFromCategory(customerId: String) {
        val newProductList = arrayListOf<ProductDetails>()
        val productRef = firebaseFireStore.collection(PRODUCT)
        productRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(TAG, "Document Product Snapshot Data: ")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if (customerId == document.getString(
                            "customerId")
                    ) {
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
                    } else {


                    }
                }
            }
            Log.d(TAG, "Product Details Document Snapshot")

        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }

        Log.d(TAG, "product List : " + productList.value)


    }


    fun addProductDetailsWithId(
        name: String,
        category: String,
        quantity: String,
        price: String,
        total: String,
        customerId: String,
        customerDetails: FirestoreCustomerDetails,
        dateTime: String,
        navController: NavController,
    ) {

        firebaseFireStore.collection(PRODUCT)
            .orderBy("productId", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    pid += id.toInt()
                    addProductDetails(pid + 1,
                        name,
                        category,
                        quantity,
                        price,
                        total,
                        customerId,
                        customerDetails,
                        dateTime,
                        navController)
                }
            }
        //addProductDetails(1000 ,name, category, quantity, price, total, customerId, customerDetails, navController)

    }


    fun addProductDetails(
        pid: Int,
        name: String,
        category: String,
        quantity: String,
        price: String,
        total: String,
        customerId: String,
        customerDetails: FirestoreCustomerDetails,
        dateTime: String,
        navController: NavController,
    ) {
        // val productDetails = ProductDetails(pid.toLong(),category,name,quantity,price,total,customerId,"","","")
        val productDetails =
            Product(pid, category, name, quantity, price, total, customerId, dateTime, "", "")
        //dbRepository.insertProduct(productDetails)
        firebaseFireStore.collection(PRODUCT).document(pid.toString())
            .set(productDetails)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot Product Details successfully written!")
                loadProductDetails(pid.toString(), customerDetails, navController)
                /* val action =
                     ProductInsertFragmentDirections.actionProductInsertFragmentToCustomerDetailsFragment(
                         customerDetails
                     )
                 navController.navigate(action)*/
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing ProductDetails document",
                    e)
            }


    }


    fun loadProductDetails(
        productId: String,
        customerDetails: FirestoreCustomerDetails,
        navController: NavController,
    ) {
        val productRef = firebaseFireStore.collection(PRODUCT)
        productRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(TAG, "Document Product Snapshot Data: ")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if (productId == document.get(
                            "productId").toString()
                    ) {
                        val productDetails = document.toObject(ProductDetails::class.java)
                        insertRemainderIntoFireStoreById(customerDetails,
                            productDetails,
                            navController)
                    }
                }
            }
            Log.d(TAG, "Product Details Document Snapshot")

        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }

    }


    fun insertRemainderIntoFireStoreById(
        customerDetails: FirestoreCustomerDetails,
        productDetails: ProductDetails,
        navController: NavController,
    ) {

        firebaseFireStore.collection(REMAINDER)
            .orderBy("remainderId", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    rid += id.toInt()
                    insertRemainderIntoFireStore(rid + 1,
                        customerDetails,
                        productDetails,
                        navController)
                }
            }
        //insertRemainderIntoFireStore(1000,customerDetails, productDetails,navController)

    }

    fun insertRemainderIntoFireStore(
        id: Int,
        customerDetails: FirestoreCustomerDetails,
        productDetails: ProductDetails,
        navController: NavController,
    ) {
        val remainderDetails = Remainder(id,
            productDetails.productId.toString(),
            productDetails.productCategory,
            productDetails.productName,
            productDetails.productQuantity,
            productDetails.productPrice,
            productDetails.productTotal,
            productDetails.customerId,
            customerDetails.customerName,
            productDetails.productOrderDate,
            productDetails.productDeliveredDate,
            productDetails.productImageUrl)
        //dbRepository.insertProduct(productDetails)
        firebaseFireStore.collection(REMAINDER).document(id.toString())
            .set(remainderDetails)
            .addOnCompleteListener {
                Log.d(TAG,
                    "DocumentSnapshot Remainder Details successfully written!")
                val action =
                    ProductInsertFragmentDirections.actionProductInsertFragmentToCustomerDetailsFragment(
                        customerDetails
                    )
                navController.navigate(action)
            }
            .addOnFailureListener { e ->
                Log.d(TAG,
                    "Error writing ProductDetails document",
                    e)
            }
    }

}





