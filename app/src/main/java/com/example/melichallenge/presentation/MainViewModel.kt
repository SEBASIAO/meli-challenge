package com.example.melichallenge.presentation

import android.util.Log
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
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private var currentQuery = ""
    private var currentOffset = 0
    private val limit = 11
    private var maxOffset : Int? = null
    var isLoading = false
    var isLastPage = false


    fun searchItemsByQuery( query : String ) {
        isLoading = true
        currentQuery = query
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(query, limit, currentOffset)
            when (result.status){
                Resource.Status.SUCCESS -> {
                    _items.postValue(result.data?.items)
                    maxOffset = (result.data?.total)?.div(10)
                    currentOffset = 0
                    isLoading = false
                }
                Resource.Status.ERROR -> {
                    Log.e("MainViewModel", "Error searching items: ${result.message}")
                    isLoading = false
                }
            }
            isLastPage = false
        }
    }

    fun loadMoreItems(){
        currentOffset += 1
        isLoading = true
        viewModelScope.launch {
            val result = repository.searchItemsByQuery(currentQuery, limit, currentOffset)
            when (result.status){
                Resource.Status.SUCCESS -> {
                    _items.postValue(result.data?.items)
                    isLoading = false
                }
                Resource.Status.ERROR -> {
                    Log.e("MainViewModel", "Error searching items: ${result.message}")
                    isLoading = false
                }
            }
            isLastPage = currentOffset >= (maxOffset ?: 0)
        }
    }
}