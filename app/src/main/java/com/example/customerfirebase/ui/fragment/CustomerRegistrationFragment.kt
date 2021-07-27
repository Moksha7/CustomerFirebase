package com.example.customerfirebase.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.customerfirebase.databinding.FragmentCustomerRegistrationBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerRegistrationFragment : Fragment() {
    private var city: String = "ah"
    private var _binding: FragmentCustomerRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadLocale()
        _binding = FragmentCustomerRegistrationBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: FirebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        val firebaseCustomerDetails = FirestoreCustomerDetails()
        binding.apply {
            customerRegistration = CustomerRegisterViewModel(firebaseCustomerDetails)

            tietCustomerLocation.setOnClickListener {
                val intent = Intent(activity, LocationActivity::class.java)
                startActivity(intent)
            }

            btnCustomerRegistration.setOnClickListener {
                saveCustomerRegistration(viewModel)
            }
        }

        Toast.makeText(context, city, Toast.LENGTH_LONG).show()
        return binding.root
    }

    private fun saveCustomerRegistration(viewModel: FirebaseViewModel) {
        binding.apply {
            val customerName = tietCustomerName.text.toString()
            val customerAddress = tietCustomerAddress.text.toString()
            val customerCity = tietCustomerDistrict.text.toString()
            val customerVillage = tietCustomerVillage.text.toString()
            val customerLocation = tietCustomerLocation.text.toString()
            val customerMobile = tietCustomerMobile.text.toString()

            viewModel.autoIncrementCustId(customerName,
                customerAddress,
                customerVillage,
                customerCity,
                customerLocation,
                customerMobile, navController)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onStart() {
        super.onStart()
        loadLocale()
        Toast.makeText(context, city, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        loadLocale()
        Toast.makeText(context, city, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        loadLocale()
        binding.tietCustomerLocation.setText(city)
        Toast.makeText(context, city, Toast.LENGTH_LONG).show()
    }

    fun loadLocale() {
        val location = "city"
        val prefs = context?.getSharedPreferences("CommonPrefs",
            AppCompatActivity.MODE_PRIVATE)
        city = prefs?.getString(location, "").toString()

    }

}


