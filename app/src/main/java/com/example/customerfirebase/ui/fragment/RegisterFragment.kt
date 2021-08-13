package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var isAllFieldChecked: Boolean = false

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
                isAllFieldChecked = checkAllFields()
                if (isAllFieldChecked) {
                    val id = tietRegisterKey.text.toString()
                    val pass = tietRegisterPassword.text.toString()
                    val name = tietRegisterName.text.toString()
                    //viewModel.insertCustIdIntoRoomDB("111113")
                    //viewModel.insertCustIdIntoFireStore("111113")
                    viewModel.checkCustIdIntoRoomDB(id, name, pass)
                    viewModel.checkCustIdIntoFireStore(it, id, name, pass)
                }
            }

            tvRegisterLoginnow.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }


        return binding.root
    }

    private fun checkAllFields(): Boolean {
        val strCid: String = binding.tilRegisterKey.editText?.text.toString()
        val strCname: String = binding.tilRegisterName.editText?.text.toString()
        val strCpass: String = binding.tilRegisterPassword.editText?.text.toString()

        if (!TextUtils.isEmpty(strCid)) {
            binding.tilRegisterKey.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCname)) {
            binding.tilRegisterName.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCpass)) {
            binding.tilRegisterPassword.isErrorEnabled = false
            return true
        } else {
            binding.tilRegisterKey.error = "Input required"
            binding.tilRegisterKey.isErrorEnabled = true
            binding.tilRegisterName.error = "Input required"
            binding.tilRegisterName.isErrorEnabled = true
            binding.tilRegisterPassword.error = "Input required"
            binding.tilRegisterPassword.isErrorEnabled = true
            return false
        }
    }


}
