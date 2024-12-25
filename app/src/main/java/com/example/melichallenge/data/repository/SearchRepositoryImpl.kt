package com.example.melichallenge.data.repository

import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.data.remote.datasource.SearchRemoteDataSource
import com.example.melichallenge.data.remote.models.mappers.toBo
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.ItemList
import com.example.melichallenge.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteDataSource: SearchRemoteDataSource) :
    SearchRepository {
    override suspend fun searchItemsByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ): Resource<ItemList> {
        val response = remoteDataSource.searchItemByQuery(query, limit, offset)
        return if (response.status == Resource.Status.SUCCESS && response.data?.results != null) {
            Resource.success(ItemList(
                items = response.data.results.map { it.toBo() },
                total = response.data.paging?.total
            ))
        } else {
            Resource.error(response.message.orEmpty())
        }
    }

    override suspend fun getItemDetails(
        id: String,
    ): Resource<Item> {
        val response = remoteDataSource.getItemDetails(id)
        return if (response.status == Resource.Status.SUCCESS && response.data != null) {
            Resource.success(response.data.toBo())
        } else {
            Resource.error(response.message.orEmpty())
        }
    }
}