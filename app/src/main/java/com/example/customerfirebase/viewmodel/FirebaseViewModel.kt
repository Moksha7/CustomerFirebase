package com.example.customerfirebase.viewmodel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.customerfirebase.db.DbRepository
import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.example.customerfirebase.ui.fragment.LoginFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


var maxid: Int = 2000

@HiltViewModel
class FirebaseViewModel @Inject
constructor(
    @ApplicationContext private val context: Context,
    private val dbRepository: DbRepository,
    private val firebaseFireStore: FirebaseFirestore,
) : ViewModel() {

    fun insertCustDataIntoRoomDB(id: String, name: String, pass: String) {
        val customerData = CustomerData(id, name, pass)
        dbRepository.insertCustomer(customerData)
        Log.d("LoginFragment", "Cust data added")
    }

    fun insertCustIdIntoRoomDB(id: String) {
        val cId = Customer(id)
        dbRepository.insertCustId(cId)
        Log.d("LoginFragment", "cust id added")
    }

    fun checkCustIdIntoRoomDB(id: String, name: String, pass: String) {
        val cId = dbRepository.getCustomerById(id)
        if (cId != null) {
            insertCustDataIntoRoomDB(id, name, pass)
            //insertCustDataIntoFireStore(id, name, pass)
            Log.d("LoginFragment", "User is valid")
        } else {
            Log.d("LoginFragment", "User is not valid")
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
                Log.d("LoginFragment",
                    "DocumentSnapshot custData successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d("LoginFragment",
                    "Error writing custData document",
                    e)
            }
    }

    fun insertCustomerDetailsIntoFireStore(
        customerName: String,
        customerAddress: String,
        customerVillage: String,
        customerCity: String,
        customerLocation: String,
        customerMobile: String,
    ) {
        //saveCustId(maxid.toString())
        loadCustId()
        val customerDetails = hashMapOf(
            "customerId" to maxid,
            "customerName" to customerName,
            "customerAddress" to customerAddress,
            "customerVillage" to customerVillage,
            "customerCity" to customerCity,
            "customerLocation" to customerLocation,
            "customerMobile" to customerMobile
        )

        firebaseFireStore.collection("customerDetails").document(maxid.toString())
            .set(customerDetails)
            .addOnCompleteListener {
                Log.d("LoginFragment",
                    "DocumentSnapshot CustomerDetails successfully written!")
                maxid = maxid + 1
                saveCustId(maxid.toString())
            }
            .addOnFailureListener { e ->
                Log.d("LoginFragment",
                    "Error writing CustomerDetails document",
                    e)
            }

    }

    fun loadCustId() {
        val location = "customerId"
        val prefs = context.getSharedPreferences("CustomerId",
            AppCompatActivity.MODE_PRIVATE)
        val city = prefs?.getString(location, "").toString()
        maxid = city.toInt()
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
                Log.d("LoginFragment",
                    "DocumentSnapshot custId successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d("LoginFragment",
                    "Error writing custId document",
                    e)
            }
    }

    fun checkCustIdIntoFireStore(id: String, name: String, pass: String) {
        val custIdRef = firebaseFireStore.collection("customer")
        custIdRef.document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("LoginFragment", "Document Snapshot Data: ${document.data}")
                val ccid = document.getString("cid")
                if (ccid == id) {
                    insertCustDataIntoFireStore(id, name, pass)
                    Log.d("LoginFragment", "User Valid")
                } else {
                    Log.d("LoginFragment", "User not Valid")
                }
            } else {
                Log.d("LoginFragment", "No such Document Snapshot")
            }
        }.addOnFailureListener { exception ->
            Log.d("LoginFragment", "get failed with", exception)
        }
    }

    fun checkLoginDataIntoFireStore(id: String, pass: String, navController: NavController) {
        val custLoginRef = firebaseFireStore.collection("customerData")

        custLoginRef.document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("LoginFragment", "Document Snapshot Data: ${document.data}")
                val ccid = document.getString("cid")
                val ccpass = document.getString("cpass")
                if (ccid == id && ccpass == pass) {
                    Log.d("LoginFragment", "User Valid")
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToCustomerRegistrationFragment()
                    navController.navigate(action)

                } else {
                    Log.d("LoginFragment", "User not Valid")
                }
            } else {
                Log.d("LoginFragment", "No such Document Snapshot")
            }
        }.addOnFailureListener { exception ->
            Log.d("LoginFragment", "get failed with", exception)
        }


    }

}