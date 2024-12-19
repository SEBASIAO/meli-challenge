package com.example.melichallenge.data.remote.models

import com.google.gson.annotations.SerializedName

data class SearchItemsApiModel(
    @SerializedName("paging") val paging: PagingApiModel? = null,
    @SerializedName("results") val results: List<ItemApiModel>? = null,
)

data class PagingApiModel(
    @SerializedName("total") var total: Int? = null,
    @SerializedName("primary_results") var primaryResults: Int? = null,
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("limit") var limit: Int? = null

)