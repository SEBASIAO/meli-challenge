package com.example.melichallenge.data.remote.services

import com.example.melichallenge.data.remote.models.SearchItemsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiServices {
    @GET("/search")
    suspend fun searchItemsByQuery(
        @Query("q") query: String,
    ): Response<SearchItemsApiModel>
}