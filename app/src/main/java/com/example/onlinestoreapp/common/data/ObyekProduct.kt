package com.example.onlinestoreapp.common.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObyekProduct(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var totalVariant: Int = 0,
    var totalStok: Int = 0,
    var price: Int = 0,
    var image: String = "",
    var variant: String = "",
) : Parcelable