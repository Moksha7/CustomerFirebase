package com.example.customerfirebase.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.customerfirebase.databinding.FragmentCustomerDetailsBinding
import com.example.customerfirebase.utils.AppBarStateChangeListener
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerDetailsFragment : Fragment() {
    val TAG = "Customer Details Fragment"
    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: CustomerRegisterViewModel

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

        val safeArgs: CustomerDetailsFragmentArgs by navArgs()
        val detailsCustomer = safeArgs.customerDetails

        binding.customerDetails = CustomerRegisterViewModel(detailsCustomer)
        viewModel = CustomerRegisterViewModel(detailsCustomer)
        var custId: Long = viewModel.customerId
        Log.d(TAG, "Customer Id" + custId.toString())

        /*(activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)*/

        binding.collapsingToolbar.title = viewModel.customerName
        binding.collapsingToolbar.setContentScrimColor(Color.GRAY)

        binding.fabAddCategory.setOnClickListener {
            val action =
                CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToCategoryFragment(
                    custId.toString())
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


}




