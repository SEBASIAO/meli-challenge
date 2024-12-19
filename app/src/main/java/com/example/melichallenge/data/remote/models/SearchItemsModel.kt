package com.example.melichallenge.data.remote.models

import com.google.gson.annotations.SerializedName

data class SearchItemsModelDTO(
    @SerializedName("paging") val paging: PagingDTO? = null,
    @SerializedName("results") val results: List<ItemModelDTO>? = null,
)

data class PagingDTO(
    @SerializedName("total") var total: Int? = null,
    @SerializedName("primary_results") var primaryResults: Int? = null,
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("limit") var limit: Int? = null

)