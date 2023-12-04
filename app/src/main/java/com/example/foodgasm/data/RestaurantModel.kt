package com.example.foodgasm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestaurantModel(
    var id: String,
    var category: String,
    var name: String,
    var address: String,
    var duration: String,
    var discount: String,
    var image: String,
    var isExpandable:Boolean
) : Parcelable {
    constructor() : this(
        id = "",
        category = "",
        name = "",
        address = "",
        duration = "",
        discount = "",
        isExpandable=false,
        image = ""
    )
}
