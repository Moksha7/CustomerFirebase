package com.example.customerfirebase.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.customerfirebase.R
import com.google.android.material.button.MaterialButton

class NetworkAlertDialog {
    companion object {
        fun lostNetwork(context: Context) {
            val builder = context.let { AlertDialog.Builder(it) }
            builder.setMessage("Please connect to the internet to proceed further")

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_check_internet, null)
            builder.setView(view)

            var buttonRetry: MaterialButton = view.findViewById(R.id.mbRetry)


            val dialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)
            dialog.window?.setGravity(Gravity.CENTER)

            buttonRetry.setOnClickListener {

            }
        }
    }

}

