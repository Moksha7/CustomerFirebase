package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.customerfirebase.databinding.FragmentRegistrationBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        binding.apply {
            btnRegisterLogin.setOnClickListener {
                Toast.makeText(context, "btnLoginClicked", Toast.LENGTH_LONG).show()
                val id = tietRegisterKey.text.toString()
                val pass = tietRegisterPassword.text.toString()
                val name = tietRegisterName.text.toString()
                //viewModel.insertCustIdIntoRoomDB("111113")
                //viewModel.insertCustIdIntoFireStore("111113")
                viewModel.checkCustIdIntoRoomDB(id, name, pass)
                viewModel.checkCustIdIntoFireStore(id, name, pass)
            }

            tvRegisterLoginnow.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }


        return binding.root
    }


}
