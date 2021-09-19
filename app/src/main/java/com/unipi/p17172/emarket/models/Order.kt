package com.unipi.p17172.emarket.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

/**
 * A data model class with required fields.
 */
@Keep
@Parcelize
@IgnoreExtraProperties
data class Order(
    var userId: String = "",
    val address: Address = Address(),

    val cartItems: ArrayList<Cart> = ArrayList(),
    val title: String = "",

    val subTotalAmount: String = "",
    val deliveryCharge: String = "",
    val totalAmount: String = "",

    val paymentMethod: String = "",
    val orderStatus: Int = 0,

    @ServerTimestamp
    val orderDate: Date = Date(),
    var id: String = ""
) : Parcelable
