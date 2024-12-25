package com.example.melichallenge.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melichallenge.databinding.ActivityMainBinding
import com.example.melichallenge.presentation.adapters.ItemAdapter
import com.example.melichallenge.presentation.adapters.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        setUpItemsRecyclerView()
        viewModel.searchItemsByQuery("Iphone")
    }

    private fun setUpItemsRecyclerView() {
        val adapter = ItemAdapter { itemClicked ->
            viewModel.getItemDetails(itemClicked.id.orEmpty())
        }
        val layoutManager = LinearLayoutManager(this)
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = layoutManager

        binding.searchRv.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = isLoading

        })

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainActivityUiState.Error -> {
                    isLoading = false
                    Log.e("MainActivity", state.message)
                }

                MainActivityUiState.Loading -> {
                    isLoading = true
                }

                is MainActivityUiState.Success -> {
                    adapter.appendList(state.data)
                    isLoading = false
                }
            }
        }

        viewModel.navigationEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { itemDetails ->
                Log.i("MainActivity", "Navigation event to details: $itemDetails")
            }
        }
    }
}

sealed class MainActivityUiState<out T> {
    object Loading : MainActivityUiState<Nothing>()

    data class Success<T>(val data: T) : MainActivityUiState<T>()

    data class Error(val message: String) : MainActivityUiState<Nothing>()

}

class MainActivityEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}