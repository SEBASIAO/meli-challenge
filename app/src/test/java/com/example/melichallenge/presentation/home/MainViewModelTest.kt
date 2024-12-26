package com.example.melichallenge.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.ItemList
import com.example.melichallenge.domain.models.PictureModel
import com.example.melichallenge.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val repository: SearchRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `searchItemsByQuery emits SuccessNewItems when repository returns success`() = runTest {
        // Arrange
        val items = listOf(Item(id = "1", title = "Item 1"))
        val resource = Resource.success(ItemList(items, 100))
        coEvery { repository.searchItemsByQuery(any(), any(), any()) } returns resource

        // Act
        viewModel.searchItemsByQuery("query")

        // Assert
        assertEquals(MainActivityUiState.LoadingNewItems, viewModel.uiState.value)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MainActivityUiState.SuccessNewItems(items), viewModel.uiState.value)
        assertEquals(items, viewModel.currentItems.first())
    }

    @Test
    fun `searchItemsByQuery emits Error when repository returns error`() = runTest {
        // Arrange
        val errorMessage = "Error occurred"
        val resource = Resource.error(errorMessage, null)
        coEvery { repository.searchItemsByQuery(any(), any(), any()) } returns resource

        // Act
        viewModel.searchItemsByQuery("query")

        // Assert
        assertEquals(MainActivityUiState.LoadingNewItems, viewModel.uiState.value)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MainActivityUiState.Error(errorMessage), viewModel.uiState.value)
    }

    @Test
    fun `loadMoreItems appends data when repository returns success`() = runTest {
        // Arrange
        val initialItems = listOf(Item(id = "1", title = "Item 1"))
        val moreItems = listOf(Item(id = "2", title = "Item 2"))
        val resourceInitial = Resource.success(ItemList(initialItems, 100))
        val resourceMoreItems = Resource.success(ItemList(moreItems, 100))
        coEvery { repository.searchItemsByQuery("query", 11, 0) } returns resourceInitial
        coEvery { repository.searchItemsByQuery("query", 11, 1) } returns resourceMoreItems

        // Act
        viewModel.searchItemsByQuery("query")
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadMoreItems()

        // Assert
        assertEquals(MainActivityUiState.LoadingMoreItems, viewModel.uiState.value)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MainActivityUiState.SuccessMoreItems(moreItems), viewModel.uiState.value)
        assertEquals(initialItems + moreItems, viewModel.currentItems.first())
    }

    @Test
    fun `getItemDetails emits navigation event when repository returns success`() = runTest {
        // Arrange
        val item = Item(id = "1", title = "Item 1")
        val details = item.copy(pictures = listOf(PictureModel(url = "https://http2.mlstatic.com/D_991753-MLA80658509187_112024-O.jpg")))
        val resource = Resource.success(details)
        coEvery { repository.getItemDetails(any()) } returns resource

        val observer: Observer<MainActivityEvent<Item>> = mockk(relaxed = true)
        viewModel.navigationEvent.observeForever(observer)

        // Act
        viewModel.getItemDetails(item)

        // Assert
        assertEquals(MainActivityUiState.Loading, viewModel.uiState.value)
        testDispatcher.scheduler.advanceUntilIdle()
        verify { observer.onChanged(match { it.getContentIfNotHandled() == details }) }
        assertEquals(MainActivityUiState.Idle, viewModel.uiState.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}