package com.example.melichallenge.data.remote.datasource

import com.example.melichallenge.data.remote.models.ItemApiModel
import com.example.melichallenge.data.remote.models.PagingApiModel
import com.example.melichallenge.data.remote.models.SearchItemsApiModel
import com.example.melichallenge.data.remote.services.SearchApiServices
import com.example.melichallenge.data.repository.SearchRepositoryImpl
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.ItemList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class SearchRemoteDataSourceTest {

    private lateinit var dataSource: SearchRemoteDataSource
    private val apiService: SearchApiServices = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dataSource = SearchRemoteDataSource(apiService)
    }

    @Test
    fun `searchItemByQuery data source map Response into Resource class`() = runTest {
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

        coEvery { apiService.searchItemsByQuery("query", 11, 0) } returns Response.success(response)

        //Act
        val result = dataSource.searchItemByQuery("query", 11, 0)

        //Assert
        assertEquals(result.status, expected.status)
        assertEquals(result.data?.results?.first()?.id, expected.data?.items?.first()?.id)
    }

    @Test
    fun `getItemDetail data source map Response into Resource class`() = runTest {
        //Arrange
        val expected = Resource.success(Item(id = "1", title = "Item 1"))

        val response = ItemApiModel(id = "1", title = "Item 1")

        coEvery { apiService.getItemDetails("1") } returns Response.success(response)

        //Act
        val result = dataSource.getItemDetails("1")

        //Assert
        assertEquals(result.status, expected.status)
        assertEquals(result.data?.id, expected.data?.id)
    }

    @Test
    fun `network call throw UnknownHostException and return Resource Error`() = runTest {
        //Arrange
        val expected = Resource.error<Nothing>("No internet connection")

        coEvery { apiService.getItemDetails("1") } throws UnknownHostException()

        //Act
        val result = dataSource.getItemDetails("1")

        //Assert
        assertEquals(result.message, expected.message)
    }

    @Test
    fun `network call throw any Exception and return Resource Error`() = runTest {
        //Arrange
        val expected = Resource.error<Nothing>("Network Call Error: ")

        coEvery { apiService.getItemDetails("1") } throws Exception()

        //Act
        val result = dataSource.getItemDetails("1")

        //Assert
        assertEquals(result.message, expected.message)
    }
}