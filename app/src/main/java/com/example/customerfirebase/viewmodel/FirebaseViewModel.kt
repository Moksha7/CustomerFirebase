package com.example.customerfirebase.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.customerfirebase.utils.FirebaseQueryLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseViewModel : ViewModel() {
    private val cust_id_ref = FirebaseDatabase.getInstance().getReference("/custId")
    private val cust_ref = FirebaseDatabase.getInstance().getReference("/custName")


    private val custIdLiveData: FirebaseQueryLiveData = FirebaseQueryLiveData(cust_id_ref)
    private val custLiveData: FirebaseQueryLiveData = FirebaseQueryLiveData(cust_ref)

    fun getCustIdDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return custIdLiveData
    }

    fun getCustDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return custLiveData
    }


    fun insertDataIntoFireStore(id: String, name: String, pass: String) {
        val customerListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               // val customer = Customer(id, name, pass)
                // Get Post object and use the values to update the UI
                // cust_ref.setValue(customer)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        cust_ref.addValueEventListener(customerListener)
    }


    fun checkDataFromFireStore(cId: String, cPass: String) {
        cust_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val id = snapshot.child("cid").getValue(String::class.java)
                val name = snapshot.child("cname").getValue(String::class.java)
                val pass = snapshot.child("cpass").getValue(String::class.java)
                if (id == cId && pass == cPass) {
                    Log.d("LoginFragment", "value same" + id + cId)
                } else {
                    Log.d("LoginFragment", "Value not same")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "onCancelled ${error.message}")
            }
        })

    }

    fun checkCustIdFromFireStore(cId: String, cPass: String, cName: String) {
        cust_ref.child("cid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("firebase", "onDataChange ${snapshot.value.toString()}")
                Log.d("LoginFragment", snapshot.value.toString())

                val id = snapshot.getValue(String::class.java)

                if (id == cId) {
                    insertDataIntoFireStore(cId, cName, cPass)
                    Log.d("LoginFragment", "Customer Valid" + id + snapshot.value.toString())
                } else {
                    Log.d("LoginFragment", "Customer Not Valid" + "Value not same")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "onCancelled ${error.message}")
            }
        })
    }


}