package com.example.melichallenge.data.remote.datasource

import com.example.melichallenge.data.remote.services.SearchApiServices
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(private val services: SearchApiServices) :
    BaseRemoteDataSource() {

    suspend fun searchItemByQuery(
        query: String,
    ) = getResult { services.searchItemsByQuery(query) }
}