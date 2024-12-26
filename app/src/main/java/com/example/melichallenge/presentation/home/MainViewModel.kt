package com.example.melichallenge.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.data.remote.datasource.Resource
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {
    private val _uiState = MutableLiveData<MainActivityUiState<List<Item>>>()
    val uiState: LiveData<MainActivityUiState<List<Item>>> get() = _uiState

    private val _navigationEvent = MutableLiveData<MainActivityEvent<Item>>()
    val navigationEvent: LiveData<MainActivityEvent<Item>> get() = _navigationEvent

    private var currentQuery = ""
    private var currentOffset = 0
    private val limit = 11
    private var maxOffset: Int? = null
    var isLastPage = false


    fun searchItemsByQuery(query: String) {
        _uiState.value = MainActivityUiState.Loading
        currentQuery = query
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(query, limit, currentOffset)
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.items?.let { _uiState.postValue(MainActivityUiState.SuccessNewItems(it)) }
                        ?: run { _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty())) }
                    maxOffset = (result.data?.total)?.div(10)
                    currentOffset = 0
                }

                Resource.Status.ERROR -> {
                    _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty()))
                }
            }
            isLastPage = false
        }
    }

    fun loadMoreItems() {
        _uiState.value = MainActivityUiState.Loading
        currentOffset += 1
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(currentQuery, limit, currentOffset)
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    result.data?.items?.let { _uiState.postValue(MainActivityUiState.SuccessMoreItems(it)) }
                        ?: run { _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty())) }
                }

                Resource.Status.ERROR -> {
                    _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty()))
                }
            }
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
                        _navigationEvent.postValue(MainActivityEvent(item.copy(pictures = it.pictures)))
                    } ?: run {
                        _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty()))
                    }
                }
                Resource.Status.ERROR -> {
                    _uiState.postValue(MainActivityUiState.Error(result.message.orEmpty()))
                }
            }
        }
    }
}