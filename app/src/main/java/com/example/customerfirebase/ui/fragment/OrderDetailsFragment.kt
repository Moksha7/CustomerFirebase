package com.example.customerfirebase.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.customerfirebase.R
import com.example.customerfirebase.adapter.OrderAdapter
import com.example.customerfirebase.databinding.FragmentOrderDetailsBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.model.OrderDetails
import com.example.customerfirebase.viewmodel.CustomerRegisterViewModel
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class OrderDetailsFragment : Fragment(), OrderAdapter.OnClickListener {
    val TAG = "Customer Details Fragment"
    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: CustomerRegisterViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    var customerId: String = ""
    var spanCount: Int = 1
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var cardViews: List<CardView>? = null
    var adapter: OrderAdapter? = null
    lateinit var customerDetails: FirestoreCustomerDetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        firebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        navController = findNavController()

        var b: Bundle? = this.arguments
        customerDetails = b?.getParcelable("CustomerDetailsArgs")!!

        binding.customerDetails = CustomerRegisterViewModel(customerDetails)

        viewModel = CustomerRegisterViewModel(customerDetails)

        customerId = viewModel.customerId.toString()
        preferences = context?.getSharedPreferences("saveSpanCount", MODE_PRIVATE)
        editor = preferences!!.edit()
        cardViews = ArrayList()

        setHasOptionsMenu(true)

        adapter = context?.let { OrderAdapter(this, it, ArrayList<OrderDetails>()) }

        firebaseViewModel.loadOrderDetailsOfCustomer(customerId)
        binding.recyclerViewOrder.adapter = adapter

        if (spanCount == 2) {
            loadOrderList(firebaseViewModel)
            binding.recyclerViewOrder.setLayoutManager(StaggeredGridLayoutManager(spanCount,
                StaggeredGridLayoutManager.VERTICAL))
            adapter?.notifyDataSetChanged()
        } else {
            loadOrderList(firebaseViewModel)
            binding.recyclerViewOrder.setLayoutManager(LinearLayoutManager(context))
            binding.recyclerViewOrder.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

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
                    binding.recyclerViewOrder.setLayoutManager(StaggeredGridLayoutManager(
                        spanCount,
                        StaggeredGridLayoutManager.VERTICAL))
                    adapter?.notifyDataSetChanged()
                } else {
                    binding.recyclerViewOrder.setLayoutManager(LinearLayoutManager(context))
                    binding.recyclerViewOrder.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
                if (Objects.equals(item.icon.constantState,
                        Objects.requireNonNull(context?.let { getDrawable(it, R.drawable.ic_grid) })
                            ?.getConstantState())
                ) {
                    spanCount = 2
                    item.setIcon(R.drawable.ic_list)
                    binding.recyclerViewOrder.setLayoutManager(StaggeredGridLayoutManager(
                        spanCount,
                        StaggeredGridLayoutManager.VERTICAL))
                    binding.recyclerViewOrder.adapter = adapter
                    adapter?.notifyDataSetChanged()
                } else {
                    spanCount = 1
                    item.setIcon(R.drawable.ic_grid)
                    binding.recyclerViewOrder.setLayoutManager(LinearLayoutManager(context))
                    binding.recyclerViewOrder.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
                loadOrderList(firebaseViewModel)
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

    private fun loadOrderList(viewModel: FirebaseViewModel) {
        viewModel.orderList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.mtvNoProductFound.visibility = View.GONE
                binding.recyclerViewOrder.visibility = View.VISIBLE
                binding.recyclerViewOrder.adapter = context?.let { it1 ->
                    OrderAdapter(this, it1, it)
                }
            } else {
                binding.mtvNoProductFound.visibility = View.VISIBLE
                binding.recyclerViewOrder.visibility = View.GONE
            }
        })
        adapter?.notifyDataSetChanged()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onClick(orderDetails: OrderDetails) {

        /* val action =
             CustomerDetailFragmentDirections.actionCustomerDetailFragmentToProductDetailsFragment(
                 productDetails)
         findNavController().navigate(action)*/
        Toast.makeText(context, orderDetails.productName, Toast.LENGTH_LONG).show()
    }


}




