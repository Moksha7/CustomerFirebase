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
import com.example.customerfirebase.databinding.FragmentProductDetailsBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    val TAG = "Product Fragment"
    private var _binding: FragmentProductDetailsBinding? = null
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

        _binding = FragmentProductDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        val safeArgs: ProductDetailsFragmentArgs by navArgs()
        val detailsProduct = safeArgs.productDetails

        binding.mtvProductNam.text



        binding.productDetails = ProductDetailsViewModel(detailsProduct)


        val viewModel: FirebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)


        return binding.root
    }


}




