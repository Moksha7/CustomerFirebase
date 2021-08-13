package com.example.customerfirebase.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.customerfirebase.R
import com.example.customerfirebase.utils.NetworkChangedListener
import com.example.customerfirebase.utils.NetworkConnectionLiveData
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    var networkChangedListener: NetworkChangedListener = NetworkChangedListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTheme(R.style.AppTheme)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()


        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        NetworkConnectionLiveData(this)
            .observe(this, Observer { isConnected ->
                if (!isConnected) {
                    // Internet Not Available
                    //NetworkAlertDialog.lostNetwork(this)
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Please connect to the internet to proceed further")

                    val view =
                        LayoutInflater.from(this).inflate(R.layout.dialog_check_internet, null)
                    builder.setView(view)

                    val buttonRetry: MaterialButton = view.findViewById(R.id.mbRetry)


                    val dialog = builder.create()
                    dialog.show()
                    dialog.setCancelable(false)
                    dialog.window?.setGravity(Gravity.CENTER)

                    buttonRetry.setOnClickListener {
                        NetworkConnectionLiveData(this)
                            .observe(this, { isConnected ->
                                if (!isConnected) {
                                    dialog.show()
                                } else {
                                    dialog.dismiss()
                                }
                            })
                    }
                    return@Observer
                }
                // Internet Available
            })
    }

}