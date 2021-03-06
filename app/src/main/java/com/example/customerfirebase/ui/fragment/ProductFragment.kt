package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerfirebase.adapter.ProductAdapter
import com.example.customerfirebase.databinding.FragmentProductBinding
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(), ProductAdapter.OnClickListener {
    val TAG = "Product Fragment"
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var viewModel: FirebaseViewModel
    var category: String = ""
    var customerId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel.loadProductDetailsFromCategory(customerId)
        loadProductList(viewModel)
        binding.productRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.productRecyclerView.setHasFixedSize(true)

        /*binding.fabAdd.setOnClickListener {
            val action =
                ProductFragmentDirections.actionProductFragmentToProductInsertFragment(category,
                    customerId)
            navController.navigate(action)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentProductBinding.inflate(
            inflater,
            container,
            false
        )




        viewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)


        /*val safeArgs: ProductFragmentArgs by navArgs()
        category = safeArgs.productDetails
        customerId = safeArgs.categoryId*/

        return binding.root
    }


    private fun loadProductList(viewModel: FirebaseViewModel) {
        viewModel.productList.observe(viewLifecycleOwner, Observer {
            binding.productRecyclerView.adapter = context?.let { it1 ->
                ProductAdapter(this, it1, it)
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onClick(productDetails: ProductDetails) {
        /* val action =
            ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(productDetails)
        navController.navigate(action)
        Toast.makeText(context, productDetails.productName, Toast.LENGTH_LONG).show()*/
    }


}




