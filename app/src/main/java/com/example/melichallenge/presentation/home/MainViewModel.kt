package com.example.melichallenge.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState<List<Item>>>(MainActivityUiState.Idle)
    val uiState: StateFlow<MainActivityUiState<List<Item>>> get() = _uiState

    private val _currentItems = MutableStateFlow<List<Item>>(emptyList())
    val currentItems: StateFlow<List<Item>> get() = _currentItems

    private val _navigationEvent = MutableLiveData<MainActivityEvent<Item>>()
    val navigationEvent: LiveData<MainActivityEvent<Item>> get() = _navigationEvent

    private val _isLoadingMoreItems = MutableStateFlow(false)
    val isLoadingMoreItems: StateFlow<Boolean> get() = _isLoadingMoreItems

    private var currentQuery = ""
    private var currentOffset = 0
    private val limit = 11
    private var maxOffset: Int? = null
    var isLastPage = false

    fun searchItemsByQuery(query: String) {
        _uiState.value = MainActivityUiState.LoadingNewItems
        currentQuery = query
        currentOffset = 0
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(query, limit, currentOffset)
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.items?.let {
                        _currentItems.value = it
                        _uiState.value = MainActivityUiState.SuccessNewItems(it)
                        maxOffset = (result.data?.total)?.div(10)
                    } ?: run {
                        _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                    }
                }
                Resource.Status.ERROR -> {
                    _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                }
            }
            isLastPage = false
        }
    }

    fun loadMoreItems() {
        if (_isLoadingMoreItems.value || isLastPage) return
        _uiState.value = MainActivityUiState.LoadingMoreItems
        _isLoadingMoreItems.value = true

        currentOffset += 1
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(currentQuery, limit, currentOffset)
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.items?.let {
                        _currentItems.value += it
                        _uiState.value = MainActivityUiState.SuccessMoreItems(it)
                    } ?: run {
                        _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                    }
                }
                Resource.Status.ERROR -> {
                    _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                }
            }
            _isLoadingMoreItems.value = false
            isLastPage = currentOffset >= (maxOffset ?: 0)
        }
    }

    fun getItemDetails(item: Item) {
        _uiState.value = MainActivityUiState.Loading
        viewModelScope.launch {
            val result = repository.getItemDetails(item.id.orEmpty())
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.let {
                        _uiState.value = MainActivityUiState.Idle
                        _navigationEvent.postValue(MainActivityEvent(item.copy(pictures = it.pictures)))
                    } ?: run {
                        _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                    }
                }
                Resource.Status.ERROR -> {
                    _uiState.value = MainActivityUiState.Error(result.message.orEmpty())
                }
            }
        }
    }
}