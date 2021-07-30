package com.example.customerfirebase.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerfirebase.adapter.ProductAdapter
import com.example.customerfirebase.databinding.FragmentCustomerDetailsBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.utils.AppBarStateChangeListener
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerDetailsFragment : Fragment(), ProductAdapter.OnClickListener {
    val TAG = "Customer Details Fragment"
    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: CustomerRegisterViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    var category: String = ""
    var customerId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomerDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        firebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)


        val safeArgs: CustomerDetailsFragmentArgs by navArgs()
        val detailsCustomer = safeArgs.customerDetails

        binding.customerDetails = CustomerRegisterViewModel(detailsCustomer)

        viewModel = CustomerRegisterViewModel(detailsCustomer)

        customerId = viewModel.customerId.toString()
        Log.d(TAG, "Customer Id" + customerId.toString())

        //binding.mtvNoProductFound.visibility = View.GONE


        /*(activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)*/

        binding.collapsingToolbar.title = viewModel.customerName
        binding.collapsingToolbar.setContentScrimColor(Color.GRAY)


        firebaseViewModel.loadProductDetailsFromCategory(customerId)
        loadProductList(firebaseViewModel)


        val customerDetails = FirestoreCustomerDetails(viewModel.customerId,
            viewModel.customerName,
            viewModel.customerAddress,
            viewModel.customerVillage,
            viewModel.customerDistrict,
            viewModel.customerLocation,
            viewModel.customerMobile)

        binding.fabAddProduct.setOnClickListener {

            val action =
                CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToProductInsertFragment(
                    customerDetails)
            navController.navigate(action)
        }

        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state != null) {
                    Log.d("State", state.name)
                }
                when (state) {
                    State.COLLAPSED -> {
                        activity?.setTitle(viewModel.customerName)
                    }
                    State.EXPANDED -> {
                        activity?.setTitle("Customer Details")
                    }
                    State.IDLE -> {
                        activity?.setTitle(viewModel.customerName)
                    }
                }
            }

        })



        return binding.root
    }


    override fun onResume() {
        super.onResume()
    }

    private fun loadProductList(viewModel: FirebaseViewModel) {
        viewModel.productList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // binding.mtvNoProductFound.visibility = View.GONE
                binding.recyclerViewProduct.visibility = View.VISIBLE
                binding.recyclerViewProduct.adapter = context?.let { it1 ->
                    ProductAdapter(this, it1, it)
                }
            } else {
                //binding.mtvNoProductFound.visibility = View.VISIBLE
                binding.recyclerViewProduct.visibility = View.GONE
            }
        })
        binding.recyclerViewProduct.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewProduct.setHasFixedSize(true)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onClick(productDetails: ProductDetails) {
        val action =
            CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToProductDetailsFragment(
                productDetails)
        navController.navigate(action)
        Toast.makeText(context, productDetails.productName, Toast.LENGTH_LONG).show()
    }


}




