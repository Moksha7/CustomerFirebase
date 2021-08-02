package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.FragmentProductInsertBinding
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductInsertFragment : Fragment() {
    private var _binding: FragmentProductInsertBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductInsertBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: FirebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        setUpSpinner()

        binding.apply {
            insertProduct = ProductDetailsViewModel(ProductDetails())

            btnInsertProduct.setOnClickListener {
                saveProductInsert(viewModel)
            }
        }

        return binding.root
    }

    private fun saveProductInsert(viewModel: FirebaseViewModel) {
        val safeArgs: ProductInsertFragmentArgs by navArgs()
        val customerDetails = safeArgs.customerDetails
        var category = binding.sCategory.selectedItem.toString()

        binding.apply {
            val productName = tietProductName.text.toString()
            val quantity = tietProductQuantity.text.toString()
            val price = tietProductPrice.text.toString()
            val total = tietProductTotal.text.toString()

            if (customerDetails != null) {
                viewModel.addProductDetailsWithId(productName,
                    category,
                    quantity,
                    price,
                    total,
                    customerDetails.customerId.toString(),
                    customerDetails,
                    navController)

            }
        }
    }

    private fun setUpSpinner() {
        val spinner = binding.sCategory
        binding.sCategory.prompt = "Choose Category"

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.category_array,
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }
    }

    }







