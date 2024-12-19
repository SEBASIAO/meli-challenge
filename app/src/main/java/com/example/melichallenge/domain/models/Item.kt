package com.example.melichallenge.domain.models

data class Item(
    val id: String? = null,
    val title: String? = null,
    val price: Int? = null,
    val originalPrice: String? = null,
    val condition: String? = null,
    val seller: Seller? = null,
    val address: Address? = null,
    val shipping: Shipping? = null,
    val salePrice: SalePrice? = null,
)

data class SalePrice(
     val priceId: String? = null,
     val amount: Int? = null,
     val currencyId: String? = null,
     val exchangeRate: String? = null,
)

data class Shipping(
     val storePickUp: Boolean? = null,
     val freeShipping: Boolean? = null,
)

data class Seller(
     val id: Int? = null,
     val nickname: String? = null
)

data class Address(
     val stateId: String? = null,
     val stateName: String? = null,
     val cityId: String? = null,
     val cityName: String? = null
)