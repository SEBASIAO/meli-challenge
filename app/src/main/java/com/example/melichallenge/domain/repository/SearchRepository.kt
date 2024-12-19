package com.example.melichallenge.domain.repository

import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.domain.models.Item

interface SearchRepository {
    suspend fun searchItemsByQuery( query : String ) : Resource<List<Item>>
}