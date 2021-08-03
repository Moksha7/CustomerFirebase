package com.example.customerfirebase.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.FragmentProductInsertBinding
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.example.customerfirebase.viewmodel.ProductDetailsViewModel
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.SpinnerAnimation
import com.skydoves.powerspinner.SpinnerGravity
import com.skydoves.powerspinner.createPowerSpinnerView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@AndroidEntryPoint
class ProductInsertFragment : Fragment() {
    private var _binding: FragmentProductInsertBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    var category = ""

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

        binding.apply {
            val productName = tietProductName.text.toString()
            val quantity = tietProductQuantity.text.toString()
            val price = tietProductPrice.text.toString()
            val total = tietProductTotal.text.toString()
            binding.sCategory.setOnSpinnerItemSelectedListener<String> { index, text ->
                category = text
            }
            val currentDate = LocalDate.now()
            val dateTime =
                (currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))).toString()
            if (customerDetails != null) {
                viewModel.addProductDetailsWithId(productName,
                    category,
                    quantity,
                    price,
                    total,
                    customerDetails.customerId.toString(),
                    customerDetails,
                    dateTime,
                    navController)

            }
        }
    }

    private fun setUpSpinner() {
        var spinner = binding.sCategory
        spinner = context?.let {
            createPowerSpinnerView(it) {
                setSpinnerPopupWidth(300)
                setSpinnerPopupHeight(350)
                setArrowPadding(6)
                setArrowAnimate(true)
                setArrowAnimationDuration(200L)
                setArrowGravity(SpinnerGravity.START)
                context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }
                    ?.let { setArrowTint(it) }
                setSpinnerPopupAnimation(SpinnerAnimation.BOUNCE)
                setShowDivider(true)
                setDividerColor(Color.WHITE)
                setDividerSize(2)

            }
        }!!


        val adapter = IconSpinnerAdapter(spinner)
        spinner.setSpinnerAdapter(adapter)
        spinner.getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)


        /* context?.let {
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
         }*/
    }

    }







