package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerfirebase.adapter.CustomerAdapter
import com.example.customerfirebase.databinding.FragmentCustomerDashboardBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerDashboardFragment : Fragment(), CustomerAdapter.OnClickListener {
    val TAG = "Customer Fragment"
    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: FirebaseViewModel
    var category: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel.loadCustomerFromFirestore()
        loadCustomerList(viewModel)
        binding.customerrecyclerview.layoutManager = LinearLayoutManager(context)

        binding.fabCustomerAdd.setOnClickListener {
            val action =
                CustomerDashboardFragmentDirections.actionCustomerDashboardFragmentToCustomerRegistrationFragment()
            navController.navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentCustomerDashboardBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        return binding.root
    }


    private fun loadCustomerList(viewModel: FirebaseViewModel) {

        viewModel.customerList.observe(viewLifecycleOwner, Observer {
            binding.customerrecyclerview.adapter = context?.let { it1 ->
                CustomerAdapter(this, it1, it)
            }
        })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onClick(customerDetails: FirestoreCustomerDetails) {
        val action =
            CustomerDashboardFragmentDirections.actionCustomerDashboardFragmentToCustomerDetailsFragment(
                customerDetails)
        navController.navigate(action)
        Toast.makeText(context,
            customerDetails.customerName + "  " + customerDetails.customerId,
            Toast.LENGTH_LONG).show()

    }


}




