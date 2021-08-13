package com.example.customerfirebase.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
    private var isAllFieldChecked: Boolean = false

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
                isAllFieldChecked = checkAllFields()
                if (isAllFieldChecked) {
                    saveCustomerRegistration(viewModel, it)
                }
            }
        }

        Toast.makeText(context, city, Toast.LENGTH_LONG).show()
        return binding.root
    }

    private fun saveCustomerRegistration(viewModel: FirebaseViewModel, it: View) {
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
                customerMobile, navController, it)
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

    private fun checkAllFields(): Boolean {
        val strCname: String = binding.tilCustomerName.editText?.text.toString()
        val strCadd: String = binding.tilCustomerAddress.editText?.text.toString()
        val strCvill: String = binding.tilCustomerVillage.editText?.text.toString()
        val strCdist: String = binding.tilCustomerDistrict.editText?.text.toString()
        val strCloc: String = binding.tilCustomerLocation.editText?.text.toString()
        val strCmob: String = binding.tilCustomerMobile.editText?.text.toString()

        if (!TextUtils.isEmpty(strCname)) {
            binding.tilCustomerName.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCadd)) {
            binding.tilCustomerAddress.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCvill)) {
            binding.tilCustomerVillage.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCdist)) {
            binding.tilCustomerDistrict.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCloc)) {
            binding.tilCustomerLocation.isErrorEnabled = false
            return true
        } else if (!TextUtils.isEmpty(strCmob)) {
            binding.tilCustomerMobile.isErrorEnabled = false
            return true
        } else {
            binding.tilCustomerName.error = "Input required"
            binding.tilCustomerName.isErrorEnabled = true
            binding.tilCustomerAddress.error = "Input required"
            binding.tilCustomerAddress.isErrorEnabled = true
            binding.tilCustomerVillage.error = "Input required"
            binding.tilCustomerVillage.isErrorEnabled = true
            binding.tilCustomerDistrict.error = "Input required"
            binding.tilCustomerDistrict.isErrorEnabled = true
            binding.tilCustomerLocation.error = "Input required"
            binding.tilCustomerLocation.isErrorEnabled = true
            binding.tilCustomerMobile.error = "Input required"
            binding.tilCustomerMobile.isErrorEnabled = true
            return false
        }
    }

}


