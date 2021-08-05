package com.example.customerfirebase.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.customerfirebase.R
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import java.util.*

class RemainderAdapter(
    private val listener: RemainderAdapter.OnClickListener,
    val context: Context,
    val list: ArrayList<RemainderDetails>,
    val firebaseViewModel: FirebaseViewModel,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mtvRemainderTitle: MaterialTextView = itemView.findViewById(R.id.item_reminder_title)
        var mtvRemainderDateTime: MaterialTextView =
            itemView.findViewById(R.id.item_reminder_time_date)
        var mtvRemainderRepeat: MaterialTextView =
            itemView.findViewById(R.id.item_reminder_repeat_info)
        var mcbIsActive: MaterialCheckBox = itemView.findViewById(R.id.reminder_isactive)
        var constraintRemainder: ConstraintLayout = itemView.findViewById(R.id.constraint_remainder)

        init {
            constraintRemainder.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val orderDetails = list[position]
                    listener.onClick(orderDetails)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            mtvRemainderTitle.text = recyclerViewModel.productName
            mtvRemainderDateTime.text =
                recyclerViewModel.remainderDate + recyclerViewModel.remainderTime
            mtvRemainderRepeat.text =
                recyclerViewModel.remainderRepeatValue.toString() + recyclerViewModel.remainderRepeatUnit
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val preferences: SharedPreferences = context.getSharedPreferences("saveSpanCount",
            Context.MODE_PRIVATE)
        val spanCount = preferences.getInt("spanCount", 1)
        if (spanCount == 2) {
            val v: View =
                LayoutInflater.from(context).inflate(R.layout.reminder_list_item, parent, false)
            return View1ViewHolder(v)
        } else {
            val vv: View =
                LayoutInflater.from(context).inflate(R.layout.reminder_list_item, parent, false)
            return View1ViewHolder(vv)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnClickListener {
        fun onClick(remainderDetails: RemainderDetails)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(position)
    }


}

