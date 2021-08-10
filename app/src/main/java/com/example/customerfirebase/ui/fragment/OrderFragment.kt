package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.customerfirebase.databinding.FragmentDialogOrderBinding
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class OrderFragment : Fragment() {
    val TAG = "Product Fragment"
    private var _binding: FragmentDialogOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: ProductDetailsViewModel
    var clickCount = 0
    var b = false
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDialogOrderBinding.inflate(
            inflater,
            container,
            false
        )
        firebaseViewModel =
            ViewModelProvider(this).get(FirebaseViewModel::class.java)

        val safeArgs: OrderFragmentArgs by navArgs()
        val detailsProduct = safeArgs.productDetails
        val customerDetails = safeArgs.customerDetails

        binding.productDetails = ProductDetailsViewModel(detailsProduct)
        viewModel = ProductDetailsViewModel(detailsProduct)
        binding.mtvProductName.text = viewModel.productName
        context?.let { Glide.with(it).load(viewModel.productUrl).into(binding.imageView1) }
        activity?.setTitle(viewModel.productName)

        binding.apply {

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->

                viewFlipper.displayedChild = 0
                context?.let { it1 ->
                    Glide.with(it1).load(viewModel.productUrl).into(imageView1)
                }
                Toast.makeText(context, "You clicked on rb1", Toast.LENGTH_LONG).show()
            }

            radioButton2.setOnCheckedChangeListener { buttonView, isChecked ->
                viewFlipper.displayedChild = 1
                /*context?.let { it1 ->
                    Glide.with(it1).load(viewModel.productUrl).into(imageView2)
                }*/
                Toast.makeText(context, "You clicked on rb2", Toast.LENGTH_LONG).show()
            }

            radioButton3.setOnCheckedChangeListener { buttonView, isChecked ->
                viewFlipper.displayedChild = 2
                /*context?.let { it1 ->
                    Glide.with(it1).load(viewModel.productUrl).into(imageView3)
                }*/
                Toast.makeText(context, "You clicked on rb3", Toast.LENGTH_LONG).show()
            }




            ivPlus.setOnClickListener {
                clickCount = clickCount + 1
                metQuantity.text = clickCount.toString()

                val quantity = clickCount.toString().toInt()
                val price = viewModel.productPrice.toInt()
                val total = quantity * price

                mtvProductPricee.text = viewModel.productPrice
                mtvProductTotall.text = total.toString()

                if (clickCount.toString().equals(viewModel.productQuantity)) {
                    b = true
                    metQuantity.visibility = View.VISIBLE
                    ivPlus.isClickable = false
                    ivPlus.isFocusable = false
                    mtvProductTotall.visibility = View.VISIBLE
                } else {
                    b = true
                    metQuantity.visibility = View.VISIBLE
                    ivMinus.isClickable = true
                    ivMinus.isFocusable = true
                    mtvProductTotall.visibility = View.VISIBLE
                }
            }

            ivMinus.setOnClickListener {
                clickCount = clickCount - 1
                val quantity = clickCount.toString().toInt()
                val price = viewModel.productPrice.toInt()
                val total = quantity * price

                mtvProductPricee.text = viewModel.productPrice
                mtvProductTotall.text = total.toString()

                if (clickCount.toString().equals("0")) {
                    clickCount = 0
                    metQuantity.visibility = View.GONE
                    ivMinus.isClickable = false
                    ivMinus.isFocusable = false
                    mtvProductTotall.visibility = View.GONE
                } else if (clickCount < 0) {
                    clickCount = 0
                    metQuantity.visibility = View.GONE
                    ivMinus.isClickable = false
                    ivMinus.isFocusable = false
                    mtvProductTotall.visibility = View.GONE
                } else {
                    b = true
                    metQuantity.visibility = View.VISIBLE
                    metQuantity.text = clickCount.toString()
                    ivPlus.isClickable = true
                    ivPlus.isFocusable = true
                    mtvProductTotall.visibility = View.VISIBLE
                }
            }

            mbPurchase.setOnClickListener {
                if (metQuantity.text.toString().length.equals(0) && metQuantity.text.trim()
                        .toString() == "0"
                ) {
                    b = false
                } else if (metQuantity.text.toString()
                        .toInt() > 0 && metQuantity.text.toString()
                        .toInt() <= viewModel.productQuantity.toInt()
                ) {
                    b = true
                } else if (metQuantity.text.toString() == "" && metQuantity.text.toString() == " ") {
                    b = false
                } else {
                    b = false
                }

                if (b) {
                    val productId = viewModel.productId.toString()
                    val productImage = viewModel.productUrl.toString()
                    val productCategory = viewModel.productCategory
                    val customerId = viewModel.customerId
                    var productInsertDate = viewModel.productInsertDate
                    var productDeliveredDate = viewModel.productDeliveredDate
                    val productQuantity = metQuantity.text.toString()
                    val productName = mtvProductName.text.toString()
                    val productPrice = mtvProductPricee.text.toString()
                    val productTotal = mtvProductTotall.text.toString()
                    val currentDate = LocalDate.now()
                    val productOrderDate =
                        (currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).toString()

                    if (customerDetails != null) {
                        firebaseViewModel.addOrderDetailsWithId(
                            productId,
                            productImage,
                            productName,
                            productCategory,
                            productQuantity,
                            productPrice,
                            productTotal,
                            customerId,
                            productInsertDate,
                            productDeliveredDate,
                            productOrderDate,
                            navController,
                            customerDetails)
                    }

                }

            }

        }

        return binding.root
    }


}




