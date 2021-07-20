package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.customerfirebase.databinding.FragmentLoginBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.LoginViewModel
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

        binding.apply {
            btnLoginLogin.setOnClickListener {
                Toast.makeText(context, "btnLoginClicked", Toast.LENGTH_LONG).show()
                val id = tietLoginCid.text.toString()
                val pass = tietLoginPassword.text.toString()
                viewModel.checkLoginDataIntoFireStore(id, pass)
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




