package com.example.melichallenge.data.repository

import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.data.remote.datasource.SearchRemoteDataSource
import com.example.melichallenge.data.remote.models.mappers.toBo
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteDataSource: SearchRemoteDataSource) : SearchRepository {
    override suspend fun searchItemsByQuery(query: String): Resource<List<Item>> {
        val response = remoteDataSource.searchItemByQuery(query)
        return if (response.status == Resource.Status.SUCCESS && response.data?.results != null) {
            Resource.success(response.data.results.map { it.toBo() })
        } else {
            Resource.error(response.message.orEmpty())
        }
    }
}