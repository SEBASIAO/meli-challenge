package com.example.melichallenge.data.repository

import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.data.remote.datasource.SearchRemoteDataSource
import com.example.melichallenge.data.remote.models.ItemApiModel
import com.example.melichallenge.data.remote.models.PagingApiModel
import com.example.melichallenge.data.remote.models.SearchItemsApiModel
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.ItemList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    private lateinit var repository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = SearchRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `searchItemsByQuery map data to Business Object`() = runTest {
        //Arrange
        val expected = Resource.success(
            ItemList(
                items = listOf(
                    Item(id = "1", title = "Item 1")
                )
            )
        )

        val results = ItemApiModel(id = "1", title = "Item 1")
        val paging = PagingApiModel(limit = 11, offset = 0)
        val response = SearchItemsApiModel(paging = paging, results = listOf(results))

        coEvery { remoteDataSource.searchItemByQuery("query", 11, 0) } returns Resource.success(response)

        //Act
        val result = repository.searchItemsByQuery("query", 11, 0)

        //Assert
        assertEquals(result.status, expected.status)
        assertEquals(result.data?.items?.first()?.id, expected.data?.items?.first()?.id)
    }

    @Test
    fun `getItemDetails map data to Business Object`() = runTest {
        //Arrange
        val expected = Resource.success(Item(id = "1", title = "Item 1"))

        val response = ItemApiModel(id = "1", title = "Item 1")

        coEvery { remoteDataSource.getItemDetails("1") } returns Resource.success(response)

        //Act
        val result = repository.getItemDetails("1")

        //Assert
        assertEquals(result.status, expected.status)
        assertEquals(result.data?.id, expected.data?.id)
    }
}