package com.example.foodgasm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val ownerCart: RestaurantModel,
    val restaurant : RestaurantModel,
    val quantity : Int
):Parcelable{
    constructor():this(RestaurantModel(),RestaurantModel(),1)
}
