package com.example.melichallenge.data.remote.datasource

import com.example.melichallenge.data.remote.services.SearchApiServices
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(private val services: SearchApiServices) :
    BaseRemoteDataSource() {

    suspend fun searchItemByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ) = getResult { services.searchItemsByQuery(query, limit, offset) }

    suspend fun getItemDetails(
        id: String,
    ) = getResult { services.getItemDetails(id) }
}