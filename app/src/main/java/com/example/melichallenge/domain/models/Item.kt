package com.example.melichallenge.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ItemList(
    val items : List<Item>,
    val total : Int? = null,
)
@Parcelize
data class Item(
    val id: String? = null,
    val title: String? = null,
    val price: Double? = null,
    val originalPrice: String? = null,
    val condition: String? = null,
    val seller: Seller? = null,
    val address: Address? = null,
    val shipping: Shipping? = null,
    val salePrice: SalePrice? = null,
    val thumbnail : String? = null,
    val pictures: List<PictureModel>? = null,
    val basePrice: Double? = null,
    val soldQuantity: Double? = null,
    val availableQuantity: Double? = null,
    val secureThumbnail: String? = null,
) : Parcelable

@Parcelize
data class SalePrice(
     val priceId: String? = null,
     val amount: Double? = null,
     val currencyId: String? = null,
     val exchangeRate: String? = null,
) : Parcelable

@Parcelize
data class Shipping(
     val storePickUp: Boolean? = null,
     val freeShipping: Boolean? = null,
) : Parcelable

@Parcelize
data class Seller(
     val id: Int? = null,
     val nickname: String? = null
) : Parcelable

@Parcelize
data class Address(
     val stateId: String? = null,
     val stateName: String? = null,
     val cityId: String? = null,
     val cityName: String? = null
) : Parcelable

@Parcelize
data class PictureModel(
    val id: String? = null,
    val url: String? = null
) : Parcelable