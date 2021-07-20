package com.example.customerfirebase.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.customerfirebase.db.DbRepository
import com.example.customerfirebase.model.Customer
import com.example.customerfirebase.model.CustomerData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


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
            insertCustDataIntoFireStore(id, name, pass)
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
        firebaseFireStore.collection("customer").whereEqualTo("cid", id).get()

        /*if(cid!=null){
            Log.d("LoginFragment","User is not valid")
        }else{
            insertCustDataIntoFireStore(id,name,pass)
            Log.d("LoginFragment","User is valid")
        }*/
    }

    fun checkLoginDataIntoFireStore(id: String, pass: String) {
        val custLoginRef = firebaseFireStore.collection("customerData")
        val cid = custLoginRef.whereEqualTo("cid", id)
        val cpass = custLoginRef.whereEqualTo("cpass", pass)
        if (cid != null && cpass != null) {
            Log.d("LoginFragment", "User is Valid")
        } else {
            Log.d("LoginFragment", "User is not valid")
        }
    }

}