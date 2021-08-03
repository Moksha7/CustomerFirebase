package com.example.customerfirebase.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.customerfirebase.R
import com.example.customerfirebase.adapter.Product1Adapter
import com.example.customerfirebase.databinding.FragmentCustomerDetailsBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.utils.AppBarStateChangeListener
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CustomerDetailsFragment : Fragment(), Product1Adapter.OnClickListener {
    val TAG = "Customer Details Fragment"
    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: CustomerRegisterViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    var customerId: String = ""
    var spanCount: Int = 1
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var cardViews: List<CardView>? = null
    var adapter: Product1Adapter? = null


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

        preferences = context?.getSharedPreferences("saveSpanCount", MODE_PRIVATE)
        editor = preferences!!.edit()
        cardViews = ArrayList()

        setHasOptionsMenu(true)

        adapter = context?.let { Product1Adapter(this, it, ArrayList<ProductDetails>()) }

        /*(activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)*/

        binding.collapsingToolbar.title = viewModel.customerName
        binding.collapsingToolbar.setContentScrimColor(Color.GRAY)


        firebaseViewModel.loadProductDetailsFromCategory(customerId)
        binding.recyclerViewProduct.adapter = adapter

        if (spanCount == 2) {
            loadProductList(firebaseViewModel)
            binding.recyclerViewProduct.setLayoutManager(StaggeredGridLayoutManager(spanCount,
                StaggeredGridLayoutManager.VERTICAL))
            adapter?.notifyDataSetChanged()
        } else {
            loadProductList(firebaseViewModel)
            binding.recyclerViewProduct.setLayoutManager(LinearLayoutManager(context))
            binding.recyclerViewProduct.adapter = adapter
            adapter?.notifyDataSetChanged()
        }


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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_list_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_product_list -> {
                spanCount = preferences!!.getInt("spanCount", 1)
                if (spanCount == 2) {
                    item.setIcon(R.drawable.ic_list)
                    binding.recyclerViewProduct.setLayoutManager(StaggeredGridLayoutManager(
                        spanCount,
                        StaggeredGridLayoutManager.VERTICAL))
                    adapter?.notifyDataSetChanged()
                } else {
                    binding.recyclerViewProduct.setLayoutManager(LinearLayoutManager(context))
                    binding.recyclerViewProduct.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
                if (Objects.equals(item.icon.constantState,
                        Objects.requireNonNull(context?.let { getDrawable(it, R.drawable.ic_grid) })
                            ?.getConstantState())
                ) {
                    spanCount = 2
                    item.setIcon(R.drawable.ic_list)
                    binding.recyclerViewProduct.setLayoutManager(StaggeredGridLayoutManager(
                        spanCount,
                        StaggeredGridLayoutManager.VERTICAL))
                    binding.recyclerViewProduct.adapter = adapter
                    adapter?.notifyDataSetChanged()
                } else {
                    spanCount = 1
                    item.setIcon(R.drawable.ic_grid)
                    binding.recyclerViewProduct.setLayoutManager(LinearLayoutManager(context))
                    binding.recyclerViewProduct.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
                loadProductList(firebaseViewModel)
                editor!!.putInt("spanCount", spanCount)
                editor!!.apply()
                adapter?.notifyDataSetChanged()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                    Product1Adapter(this, it1, it)
                }
            } else {
                //binding.mtvNoProductFound.visibility = View.VISIBLE
                binding.recyclerViewProduct.visibility = View.GONE
            }
        })
        adapter?.notifyDataSetChanged()

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




