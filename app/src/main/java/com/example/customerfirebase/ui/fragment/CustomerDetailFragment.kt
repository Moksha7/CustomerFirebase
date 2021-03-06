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
import com.google.android.material.appbar.AppBarLayout
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

        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                activity?.title = viewModel.customerName
                isShow = true
            } else if (isShow) {
                activity?.title =
                    " Customer Details " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })



        return binding.root
    }


    class ChildFragmentStateAdapter(
        fragment: Fragment,
        val customerDetailsArgs: FirestoreCustomerDetails,
    ) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val customerDetails = CustomerDetailsFragment()
            val b = Bundle()
            val CUSTOMER_DETAILS = "CustomerDetailsArgs"
            b.putParcelable(CUSTOMER_DETAILS, customerDetailsArgs)
            customerDetails.arguments = b


            val orderDetails = OrderDetailsFragment()
            val bb = Bundle()
            val ORDER_DETAILS = "CustomerDetailsArgs"
            bb.putParcelable(ORDER_DETAILS, customerDetailsArgs)
            orderDetails.arguments = bb

            val remainderDetails = RemainderListFragment()
            val bbb = Bundle()
            val REMAINDER_DETAILS = "CustomerDetailsArgs"
            bb.putParcelable(REMAINDER_DETAILS, customerDetailsArgs)
            remainderDetails.arguments = bbb

            return when (position) {
                0 -> orderDetails
                1 -> customerDetails
                2 -> remainderDetails
                else -> customerDetails
            }
        }

    }


}







