package com.example.foodgasm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val restaurant : RestaurantModel,
    val quantity : Int
):Parcelable{
    constructor():this(RestaurantModel(),1)
}
