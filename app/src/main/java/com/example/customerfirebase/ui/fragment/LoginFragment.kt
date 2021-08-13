package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.customerfirebase.databinding.FragmentLoginBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var isAllFieldChecked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

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

        binding.apply {
            btnLoginLogin.setOnClickListener {
                isAllFieldChecked = checkAllFields()
                if (isAllFieldChecked) {
                    val id = tietLoginCid.text.toString()
                    val pass = tietLoginPassword.text.toString()
                    viewModel.checkLoginDataIntoFireStore(it, id, pass, navController)
                }
            }

            tvLoginRegsiternow.setOnClickListener {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }
        }

        return binding.root
    }


    private fun checkAllFields(): Boolean {
        val strLoginCid: String = binding.tilLoginCid.editText?.text.toString()
        val strLoginCpass: String = binding.tilLoginPassword.editText?.text.toString()

        if (!TextUtils.isEmpty(strLoginCid)) {
            Snackbar.make(requireView(), strLoginCid, Snackbar.LENGTH_SHORT).show()
            binding.tilLoginCid.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strLoginCpass)) {
            Snackbar.make(requireView(), strLoginCid, Snackbar.LENGTH_SHORT).show()
            binding.tilLoginPassword.isErrorEnabled = false
            return true
        } else {
            binding.tilLoginCid.error = "Input required"
            binding.tilLoginCid.isErrorEnabled = true
            binding.tilLoginPassword.error = "Input required"
            binding.tilLoginPassword.isErrorEnabled = true
            return false
        }
    }

}




