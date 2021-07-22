package com.example.customerfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.customerfirebase.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        setTheme(R.style.AppTheme)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_map_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()


    }


}