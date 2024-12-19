package com.example.melichallenge.domain.repository

import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.ItemList

interface SearchRepository {
    suspend fun searchItemsByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ): Resource<ItemList>
}