package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
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
        val category = safeArgs.category
        val customerId = safeArgs.categoryId
        binding.apply {
            val productName = tietProductName.text.toString()
            val quantity = tietProductQuantity.text.toString()
            val price = tietProductPrice.text.toString()
            val total = tietProductTotal.text.toString()

            if (category != null) {
                if (customerId != null) {
                    viewModel.addProductDetails(productName,
                        category,
                        quantity,
                        price,
                        total,
                        customerId,
                        navController)
                }
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


}


