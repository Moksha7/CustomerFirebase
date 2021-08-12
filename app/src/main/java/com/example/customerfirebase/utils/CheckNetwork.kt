package com.example.customerfirebase.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

public class CheckNetwork {
    companion object {
        public fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // For 29 api or above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            // For below 29 api
            else {
                if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                    return true
                }
            }
            return false
        }
    }

    /*fun showCustomDialogForConnectivity(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Please connect to the internet to proceed further")
            .setCancelable(false)
            .setPositiveButton("Connect", DialogInterface.OnClickListener { dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            })
    }*/
}