package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.customerfirebase.databinding.FragmentLoginBinding
import com.example.customerfirebase.model.Customer
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var firebaseDatabase: FirebaseDatabase? = null

    // creating a variable for our Database
    // Reference for Firebase.
    var custbaseReference: DatabaseReference? = null
    var databaseReference: DatabaseReference? = null
    var custId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )

        firebaseDatabase = FirebaseDatabase.getInstance()

        // below line is used to get reference for our database.
        custbaseReference = firebaseDatabase!!.getReference("custId")
        databaseReference = firebaseDatabase!!.getReference("custName")

        binding.apply {
            btnLoginLogin.setOnClickListener {
                Toast.makeText(context, "btnLoginClicked", Toast.LENGTH_LONG).show()
                getDataFromFireStore()
            }
        }

        return binding.root
    }

    private fun insertDataIntoFireStore() {
        val cid: String = binding.tietLoginCid.text.toString()
        val cpass: String = binding.tietLoginPassword.text.toString()

        val customerListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val customer = Customer(cid, "abc", cpass)
                // Get Post object and use the values to update the UI
                databaseReference?.setValue(customer)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        databaseReference?.addValueEventListener(customerListener)

    }


    private fun getDataFromFireStore() {
        var cid: String = binding.tietLoginCid.text.toString()

        custbaseReference?.child("cid")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("firebase", "onDataChange ${snapshot.value.toString()}")
                Log.d("LoginFragment", snapshot.value.toString())

                if (cid == snapshot.value.toString()) {
                    Log.d("LoginFragment", cid + snapshot.value.toString())
                    insertDataIntoFireStore()
                } else {
                    Log.d("LoginFragment", "Value not same")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "onCancelled ${error.message}")
            }
        })

    }


}
