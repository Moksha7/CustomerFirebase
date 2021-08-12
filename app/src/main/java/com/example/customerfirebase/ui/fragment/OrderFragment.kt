package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.FragmentDialogOrderBinding
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.ProductDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    override fun onResume() {
        super.onResume()
        flipRadioButton()
    }

    override fun onPause() {
        super.onPause()
        flipRadioButton()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        flipRadioButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flipRadioButton()
        navController = Navigation.findNavController(view)
        val bottomSheetParent = view.findViewById<ConstraintLayout>(R.id.bottom_sheet_parent)
        val mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED

        mBottomSheetBehaviour.setPeekHeight(90)

        binding.bottomSheetParent.mbArrowDown.setOnClickListener {
            if (mBottomSheetBehaviour.state != BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }


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

        initializeValues()



        binding.apply {

            var isShow = true
            var scrollRange = -1
            binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    activity?.title = viewModel.productName
                    isShow = true
                } else if (isShow) {
                    activity?.title =
                        " Order Product " //careful there should a space between double quote otherwise it wont work
                    isShow = false
                }
            })

            viewFlipper.flipInterval = 4000
            viewFlipper.displayedChild = 0
            viewFlipper.isAutoStart = true
            viewFlipper.setInAnimation(context, android.R.anim.fade_in)
            viewFlipper.setOutAnimation(context, android.R.anim.fade_out)
            flipRadioButton()


            /*radioButton2.setOnCheckedChangeListener { buttonView, isChecked ->
                viewFlipper.displayedChild = 0
                context?.let { it1 ->
                    Glide.with(it1).load(viewModel.productUrl).into(imageView1)
                }
            }

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                viewFlipper.displayedChild = 1
            }

            radioButton3.setOnCheckedChangeListener { buttonView, isChecked ->
                viewFlipper.displayedChild = 2
            }
*/



            bottomSheetParent.ivPlus.setOnClickListener {
                clickCount = clickCount + 1
                bottomSheetParent.metQuantity.text = clickCount.toString()

                val quantity = clickCount.toString().toInt()
                val price = viewModel.productPrice.toInt()
                val total = quantity * price

                bottomSheetParent.mtvProductPricee.text = viewModel.productPrice
                bottomSheetParent.mtvProductTotall.text = total.toString()

                if (clickCount.toString().equals(viewModel.productQuantity)) {
                    b = true
                    bottomSheetParent.ivPlus.isClickable = false
                    bottomSheetParent.ivPlus.isFocusable = false

                } else {
                    b = true
                    bottomSheetParent.ivMinus.isClickable = true
                    bottomSheetParent.ivMinus.isFocusable = true
                }
            }

            bottomSheetParent.ivMinus.setOnClickListener {
                clickCount = clickCount - 1
                val quantity = clickCount.toString().toInt()
                val price = viewModel.productPrice.toInt()
                val total = quantity * price

                bottomSheetParent.mtvProductPricee.text = viewModel.productPrice
                bottomSheetParent.mtvProductTotall.text = total.toString()

                if (clickCount.toString().equals("0")) {
                    clickCount = 0
                    bottomSheetParent.ivMinus.isClickable = false
                    bottomSheetParent.ivMinus.isFocusable = false
                    bottomSheetParent.mtvProductTotall.text = "0"
                } else if (clickCount < 0) {
                    clickCount = 0
                    bottomSheetParent.ivMinus.isClickable = false
                    bottomSheetParent.ivMinus.isFocusable = false
                    bottomSheetParent.mtvProductTotall.text = "0"
                    bottomSheetParent.metQuantity.text = "0"
                } else {
                    b = true
                    bottomSheetParent.metQuantity.text = clickCount.toString()
                    bottomSheetParent.ivPlus.isClickable = true
                    bottomSheetParent.ivPlus.isFocusable = true

                }
            }


            mbPurchase.setOnClickListener {
                customerDetails?.let { it1 -> purchase(it1) }
            }
        }

        return binding.root
    }

    private fun initializeValues() {
        binding.mtvProductName.text = viewModel.productName
        context?.let { Glide.with(it).load(viewModel.productUrl).into(binding.imageView1) }
        binding.bottomSheetParent.metQuantity.text = "0"
        binding.bottomSheetParent.mtvProductPricee.text = viewModel.productPrice
        binding.bottomSheetParent.mtvProductTotall.text = "0"
    }


    override fun onStart() {
        super.onStart()
        flipRadioButton()
    }

    private fun flipRadioButton() {

        binding.apply {
            viewFlipper.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                Log.d("view flipper", viewFlipper.currentView.toString())
                if (viewFlipper.currentView == binding.linear1) {
                    Log.d("view flipper", "Image 1")
                    radioButton.isChecked = true
                    context?.let { it1 ->
                        Glide.with(it1).load(viewModel.productUrl).into(imageView1)
                    }
                } else if (viewFlipper.currentView == binding.linear2) {
                    Log.d("view flipper", "Image 2")
                    radioButton2.isChecked = true
                } else if (viewFlipper.currentView == binding.linear3) {
                    Log.d("view flipper", "Image 3")
                    radioButton3.isChecked = true
                }
            }
        }
    }

    fun purchase(customerDetails: FirestoreCustomerDetails) {
        binding.apply {
            if (bottomSheetParent.metQuantity.text.toString().length.equals(0) && bottomSheetParent.metQuantity.text.trim()
                    .toString() == "0"
            ) {
                b = false
            } else if (bottomSheetParent.metQuantity.text.toString()
                    .toInt() > 0 && bottomSheetParent.metQuantity.text.toString()
                    .toInt() <= viewModel.productQuantity.toInt()
            ) {
                b = true
            } else if (bottomSheetParent.metQuantity.text.toString() == "" && bottomSheetParent.metQuantity.text.toString() == " ") {
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
                val productDeliveredDate = viewModel.productDeliveredDate
                val productQuantity = bottomSheetParent.metQuantity.text.toString()
                val productName = mtvProductName.text.toString()
                val productPrice = bottomSheetParent.mtvProductPricee.text.toString()
                val productTotal = bottomSheetParent.mtvProductTotall.text.toString()
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



}




