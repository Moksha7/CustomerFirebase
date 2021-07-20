package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.customerfirebase.databinding.FragmentRegistrationBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.RegisterViewModel
import com.google.firebase.database.DataSnapshot

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(
            inflater,
            container,
            false
        )

        binding.registration = RegisterViewModel()

        val viewModel: FirebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        val liveDataCustId: LiveData<DataSnapshot?> = viewModel.getCustIdDataSnapshotLiveData()
        val liveDataCust: LiveData<DataSnapshot?> = viewModel.getCustDataSnapshotLiveData()

        liveDataCust.observe(viewLifecycleOwner, object : Observer<DataSnapshot?> {
            override fun onChanged(@Nullable dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    val id = dataSnapshot.child("cid").getValue(String::class.java)
                    val name = dataSnapshot.child("cname").getValue(String::class.java)
                    val pass = dataSnapshot.child("cpass").getValue(String::class.java)
                    Log.d("LoginFragment", id + name + pass)
                }
            }
        })

        liveDataCustId.observe(viewLifecycleOwner, object : Observer<DataSnapshot?> {
            override fun onChanged(@Nullable dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    val id = dataSnapshot.child("cid").getValue(String::class.java)
                    if (id != null) {
                        Log.d("LoginFragment", id)
                    }
                }
            }
        })

        binding.apply {
            btnRegisterLogin.setOnClickListener {
                Toast.makeText(context, "btnLoginClicked", Toast.LENGTH_LONG).show()
                val id = tietRegisterKey.text.toString()
                val pass = tietRegisterPassword.text.toString()
                val name = tietRegisterName.text.toString()
                viewModel.checkCustIdFromFireStore(id, pass, name)
            }

            tvRegisterLoginnow.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        return binding.root
    }
}
