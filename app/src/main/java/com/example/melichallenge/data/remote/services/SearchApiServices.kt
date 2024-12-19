package com.example.melichallenge.data.remote.services

import com.example.melichallenge.data.remote.models.SearchItemsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiServices {
    @GET("sites/MLA/search")
    suspend fun searchItemsByQuery(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<SearchItemsApiModel>
}