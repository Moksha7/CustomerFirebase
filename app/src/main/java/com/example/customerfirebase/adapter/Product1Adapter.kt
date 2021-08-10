package com.example.customerfirebase.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerfirebase.R
import com.example.customerfirebase.model.FirestoreCustomerDetails
import com.example.customerfirebase.model.ProductDetails
import com.example.customerfirebase.utils.InputFilterMinMax
import com.example.customerfirebase.viewmodel.FirebaseViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class Product1Adapter(
    private val listener: Product1Adapter.OnClickListener,
    val context: Context,
    val list: ArrayList<ProductDetails>,
    val firebaseViewModel: FirebaseViewModel,
    val navController: NavController,
    val customerDetails: FirestoreCustomerDetails,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.O)
    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mtvProductName: MaterialTextView =
            itemView.findViewById(R.id.mtvProductName) // Initialize your All views prensent in list items
        var mtvProductQuantity: MaterialTextView =
            itemView.findViewById(R.id.mtvProductQuantity)
        var mtvProductPrice: MaterialTextView =
            itemView.findViewById(R.id.mtvProductPrice)
        var mtvProductTotal: MaterialTextView =
            itemView.findViewById(R.id.mtvProductTotal)
        var mtvProductOrderDate: MaterialTextView =
            itemView.findViewById(R.id.mtvProductOrderDate)
        var imageProduct: ImageView = itemView.findViewById(R.id.image_product)
        var cardProduct: CardView = itemView.findViewById(R.id.card_view_customer)
        var mbOrder: MaterialButton = itemView.findViewById(R.id.mbPurchase)

        init {
            cardProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val productDetails = list[position]
                    listener.onClick(productDetails)
                }
            }

            mbOrder.setOnClickListener {
                var clickCount = 0
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val recyclerViewModel = list[position]
                    var b = false
                    var alert = false
                    val builder = AlertDialog.Builder(it.context)
                    val view = LayoutInflater.from(context).inflate(R.layout.dialog_order, null)
                    val mtvProductName: MaterialTextView =
                        view.findViewById(R.id.mtvProductName) // Initialize your All views prensent in list items
                    val mtvProductQuantity: MaterialTextView =
                        view.findViewById(R.id.mtvProductQuantity)
                    val mtvProductPrice: MaterialTextView =
                        view.findViewById(R.id.mtvProductPrice)
                    val mtvProductTotal: MaterialTextView =
                        view.findViewById(R.id.mtvProductTotal)
                    val mtvProductPricee: MaterialTextView =
                        view.findViewById(R.id.mtvProductPricee)
                    val mtvProductTotall: MaterialTextView =
                        view.findViewById(R.id.mtvProductTotall)
                    val imageProduct: ImageView = view.findViewById(R.id.image_product)
                    val imagePlus: ImageView = view.findViewById(R.id.ivPlus)
                    val imageMinus: ImageView = view.findViewById(R.id.ivMinus)
                    val cardProduct: CardView = view.findViewById(R.id.card_view_customer)
                    val mbOrder: MaterialButton = view.findViewById(R.id.mbPurchase)
                    val metQuantity: MaterialTextView = view.findViewById(R.id.metQuantity)
                    metQuantity.filters =
                        arrayOf(InputFilterMinMax(1, recyclerViewModel.productQuantity.toInt()))

                    builder.setView(view)
                    val alertDialog = builder.create()

                    mtvProductName.text = recyclerViewModel.productName
                    Glide.with(context).load(list[position].productImageUrl).into(imageProduct)

                    imagePlus.setOnClickListener {
                        clickCount = clickCount + 1
                        metQuantity.text = clickCount.toString()

                        val quantity = clickCount.toString().toInt()
                        val price = recyclerViewModel.productPrice.toInt()
                        val total = quantity * price

                        mtvProductPricee.text = recyclerViewModel.productPrice
                        mtvProductTotall.text = total.toString()

                        if (clickCount.toString().equals(recyclerViewModel.productQuantity)) {
                            b = true
                            metQuantity.visibility = View.VISIBLE
                            imagePlus.isClickable = false
                            imagePlus.isFocusable = false
                            mtvProductTotall.visibility = View.VISIBLE
                        } else {
                            b = true
                            metQuantity.visibility = View.VISIBLE
                            imageMinus.isClickable = true
                            imageMinus.isFocusable = true
                            mtvProductTotall.visibility = View.VISIBLE
                        }
                    }

                    imageMinus.setOnClickListener {
                        clickCount = clickCount - 1
                        val quantity = clickCount.toString().toInt()
                        val price = recyclerViewModel.productPrice.toInt()
                        val total = quantity * price

                        mtvProductPricee.text = recyclerViewModel.productPrice
                        mtvProductTotall.text = total.toString()

                        if (clickCount.toString().equals("0")) {
                            clickCount = 0
                            metQuantity.visibility = View.GONE
                            imageMinus.isClickable = false
                            imageMinus.isFocusable = false
                            mtvProductTotall.visibility = View.GONE
                        } else if (clickCount < 0) {
                            clickCount = 0
                            metQuantity.visibility = View.GONE
                            imageMinus.isClickable = false
                            imageMinus.isFocusable = false
                            mtvProductTotall.visibility = View.GONE
                        } else {
                            b = true
                            metQuantity.visibility = View.VISIBLE
                            metQuantity.text = clickCount.toString()
                            imagePlus.isClickable = true
                            imagePlus.isFocusable = true
                            mtvProductTotall.visibility = View.VISIBLE
                        }
                    }/*
                    metQuantity.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {

                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) {
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) {
                            val quantity = s.toString().toInt()
                            val price = recyclerViewModel.productPrice.toInt()
                            val total = quantity * price
                            metQuantity.text = s.toString()
                            mtvProductPricee.text = recyclerViewModel.productPrice
                            mtvProductTotall.text = total.toString()
                        }
                    })

*/

                    mbOrder.setOnClickListener {
                        if (metQuantity.text.toString().length.equals(0) && metQuantity.text.trim()
                                .toString() == "0"
                        ) {
                            b = false
                        } else if (metQuantity.text.toString()
                                .toInt() > 0 && metQuantity.text.toString()
                                .toInt() <= recyclerViewModel.productQuantity.toInt()
                        ) {
                            b = true
                        } else if (metQuantity.text.toString() == "" && metQuantity.text.toString() == " ") {
                            b = false
                        } else {
                            b = false
                        }

                        if (b) {
                            val productId = recyclerViewModel.productId.toString()
                            val productImage = recyclerViewModel.productImageUrl.toString()
                            val productCategory = recyclerViewModel.productCategory
                            val customerId = recyclerViewModel.customerId
                            var productInsertDate = recyclerViewModel.productOrderDate
                            var productDeliveredDate = recyclerViewModel.productDeliveredDate
                            val productQuantity = metQuantity.text.toString()
                            val productName = mtvProductName.text.toString()
                            val productPrice = mtvProductPricee.text.toString()
                            val productTotal = mtvProductTotall.text.toString()
                            val currentDate = LocalDate.now()
                            val productOrderDate =
                                (currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).toString()

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

                            alert = true
                        }

                        if (alert) {
                            alertDialog.dismiss()
                        }

                    }

                    alertDialog.show()
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            mtvProductName.text = recyclerViewModel.productName
            mtvProductQuantity.text = "Quantity : " + recyclerViewModel.productQuantity
            mtvProductPrice.text = "Price : " + recyclerViewModel.productPrice
            mtvProductTotal.text = "Total : " + recyclerViewModel.productTotal
            mtvProductOrderDate.text = recyclerViewModel.productOrderDate
            val bmp = stringToBitMap(list[position].productImageUrl)
            imageProduct.setImageBitmap(bmp)
            Glide.with(context).load(list[position].productImageUrl).into(imageProduct)

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val preferences: SharedPreferences = context.getSharedPreferences("saveSpanCount",
            Context.MODE_PRIVATE)
        val spanCount = preferences.getInt("spanCount", 1)
        if (spanCount == 2) {
            val v: View =
                LayoutInflater.from(context).inflate(R.layout.product_item_grid, parent, false)
            return View1ViewHolder(v)
        } else {
            val vv: View =
                LayoutInflater.from(context).inflate(R.layout.product_item_details, parent, false)
            return View1ViewHolder(vv)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToBitMap(string: String): Bitmap? {
        return try {
            val imageBytes = Base64.getDecoder().decode(string)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            scaleToFitHeight(image, 80)
        } catch (e: Exception) {
            Log.d("Image", e.message.toString())
            return null
        }
    }

    fun scaleToFitHeight(b: Bitmap, height: Int): Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, true)
    }

    interface OnClickListener {
        fun onClick(productDetails: ProductDetails)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(position)
    }


}

