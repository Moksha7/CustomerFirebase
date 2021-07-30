package com.example.customerfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.customerfirebase.R
import com.example.customerfirebase.databinding.FragmentSelectCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(),
    AdapterView.OnItemSelectedListener {
    val TAG = "Product Fragment"
    private var _binding: FragmentSelectCategoryBinding? = null
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

        _binding = FragmentSelectCategoryBinding.inflate(
            inflater,
            container,
            false
        )

        /*val safeArgs: CategoryFragmentArgs by navArgs()
        val categoryId = safeArgs.categoryId
*/
        setUpSpinner()


        binding.mbLoadProduct.setOnClickListener {
            val category = binding.categorySpinner.selectedItem.toString()
            /* val action =
                 categoryId?.let { it1 ->
                     CategoryFragmentDirections.actionCategoryFragmentToProductFragment(category,
                         it1)
                 }
             if (action != null) {
                 navController.navigate(action)
             }*/
        }



        return binding.root
    }

    private fun setUpSpinner() {
        val spinner = binding.categorySpinner

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.category_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}










