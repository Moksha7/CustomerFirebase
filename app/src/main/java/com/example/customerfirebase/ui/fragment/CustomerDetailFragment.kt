package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.customerfirebase.databinding.FragmentCustomerDetailBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerDetailFragment : Fragment() {
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: CustomerRegisterViewModel
    private lateinit var customerDetailsArgs: FirestoreCustomerDetails
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.viewPager.isUserInputEnabled = false


        binding.viewPager.adapter = ChildFragmentStateAdapter(this, customerDetailsArgs)
        // Bind tabs and viewpager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Order List"
                1 -> tab.text = "Product List"
                2 -> tab.text = "Remainder List"
            }
        }.attach()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomerDetailBinding.inflate(
            inflater,
            container,
            false
        )
        val safeArgs: CustomerDetailsFragmentArgs by navArgs()
        val detailsCustomer = safeArgs.customerDetails

        binding.customerDetails = CustomerRegisterViewModel(detailsCustomer)

        viewModel = CustomerRegisterViewModel(detailsCustomer)

        customerDetailsArgs = FirestoreCustomerDetails(viewModel.customerId,
            viewModel.customerName,
            viewModel.customerAddress,
            viewModel.customerVillage,
            viewModel.customerDistrict,
            viewModel.customerLocation,
            viewModel.customerMobile)

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL


        return binding.root
    }


    class ChildFragmentStateAdapter(
        private val fragment: Fragment,
        val customerDetailsArgs: FirestoreCustomerDetails,
    ) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            var customerDetails: CustomerDetailsFragment = CustomerDetailsFragment()
            var b: Bundle = Bundle()
            val CUSTOMER_DETAILS = "CustomerDetailsArgs"
            b.putParcelable(CUSTOMER_DETAILS, customerDetailsArgs)
            customerDetails.arguments = b

            return when (position) {
                0 -> customerDetails
                1 -> customerDetails
                2 -> customerDetails
                else -> customerDetails
            }
        }

    }


}







