package com.example.melichallenge.data.remote.models

import com.google.gson.annotations.SerializedName

data class ItemApiModel(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("condition") var condition: String? = null,
    @SerializedName("thumbnail_id") var thumbnailId: String? = null,
    @SerializedName("catalog_product_id") var catalogProductId: String? = null,
    @SerializedName("listing_type_id") var listingTypeId: String? = null,
    @SerializedName("sanitized_title") var sanitizedTitle: String? = null,
    @SerializedName("permalink") var permalink: String? = null,
    @SerializedName("buying_mode") var buyingMode: String? = null,
    @SerializedName("site_id") var siteId: String? = null,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("domain_id") var domainId: String? = null,
    @SerializedName("variation_id") var variationId: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("currency_id") var currencyId: String? = null,
    @SerializedName("order_backend") var orderBackend: Int? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("original_price") var originalPrice: String? = null,
    @SerializedName("sale_price") var salePrice: SalePriceApiModel? = null,
    @SerializedName("available_quantity") var availableQuantity: Int? = null,
    @SerializedName("official_store_id") var officialStoreId: String? = null,
    @SerializedName("use_thumbnail_id") var useThumbnailId: Boolean? = null,
    @SerializedName("accepts_mercadopago") var acceptsMercadopago: Boolean? = null,
    @SerializedName("variation_filters") var variationFilters: ArrayList<String>? = null,
    @SerializedName("shipping") var shipping: ShippingApiModel? = null,
    @SerializedName("stop_time") var stopTime: String? = null,
    @SerializedName("seller") var seller: SellerApiModel? = null,
    @SerializedName("address") var address: AddressApiModel? = null,
    @SerializedName("winner_item_id") var winnerItemId: String? = null,
    @SerializedName("catalog_listing") var catalogListing: Boolean? = null,
    @SerializedName("discounts") var discounts: String? = null,
    @SerializedName("promotion_decorations") var promotionDecorations: String? = null,
    @SerializedName("promotions") var promotions: String? = null,
    @SerializedName("inventory_id") var inventoryId: String? = null
)

data class SalePriceApiModel(
    @SerializedName("price_id") var priceId: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("currency_id") var currencyId: String? = null,
    @SerializedName("exchange_rate") var exchangeRate: String? = null,
)

data class ShippingApiModel(
    @SerializedName("store_pick_up") var storePickUp: Boolean? = null,
    @SerializedName("free_shipping") var freeShipping: Boolean? = null,
)

data class SellerApiModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nickname") var nickname: String? = null
)

data class AddressApiModel(
    @SerializedName("state_id") var stateId: String? = null,
    @SerializedName("state_name") var stateName: String? = null,
    @SerializedName("city_id") var cityId: String? = null,
    @SerializedName("city_name") var cityName: String? = null
)