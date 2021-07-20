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
import com.example.customerfirebase.databinding.FragmentLoginBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.LoginViewModel
import com.google.firebase.database.DataSnapshot
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
        val viewModel: FirebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        binding.login = LoginViewModel()
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
            btnLoginLogin.setOnClickListener {
                Toast.makeText(context, "btnLoginClicked", Toast.LENGTH_LONG).show()
                val id = tietLoginCid.text.toString()
                val pass = tietLoginPassword.text.toString()
                viewModel.checkDataFromFireStore(id, pass)
            }

            tvLoginRegsiternow.setOnClickListener {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }
        }
        return binding.root
    }
}




