package com.example.melichallenge.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melichallenge.databinding.ActivityMainBinding
import com.example.melichallenge.presentation.adapters.ItemAdapter
import com.example.melichallenge.presentation.adapters.PaginationScrollListener
import com.example.melichallenge.presentation.details.DetailsActivity
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var isLoadingMoreItems = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        setUpItemsRecyclerView()

        binding.searchTf.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchTf.text.toString()
                viewModel.searchItemsByQuery(query)
                hideKeyboard(textView)
                true
            } else {
                false
            }
        }
    }

    private fun setUpItemsRecyclerView() {
        val adapter = ItemAdapter { itemClicked ->
            viewModel.getItemDetails(itemClicked)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = layoutManager

        binding.searchRv.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = isLoadingMoreItems

        })

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainActivityUiState.Error -> {
                    binding.progressCircular.isVisible = false
                    binding.shimmerListContainer.root.isVisible = false
                    isLoadingMoreItems = false
                    adapter.hideLoading()
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }

                is MainActivityUiState.SuccessMoreItems -> {
                    adapter.appendList(state.data)
                    binding.progressCircular.isVisible = false
                    binding.shimmerListContainer.root.isVisible = false
                    isLoadingMoreItems = false
                    adapter.hideLoading()
                }

                is MainActivityUiState.SuccessNewItems -> {
                    adapter.setNewItems(state.data)
                    binding.progressCircular.isVisible = false
                    binding.shimmerListContainer.root.isVisible = false
                    isLoadingMoreItems = false
                    adapter.hideLoading()
                }

                MainActivityUiState.LoadingMoreItems -> {
                    binding.progressCircular.isVisible = false
                    binding.shimmerListContainer.root.isVisible = false
                    isLoadingMoreItems = true
                    adapter.showLoading()
                }

                MainActivityUiState.LoadingNewItems -> {
                    binding.progressCircular.isVisible = false
                    binding.shimmerListContainer.root.isVisible = true
                    isLoadingMoreItems = false
                    adapter.hideLoading()
                }

                MainActivityUiState.Loading -> {
                    binding.progressCircular.isVisible = true
                    binding.shimmerListContainer.root.isVisible = false
                    isLoadingMoreItems = false
                    adapter.hideLoading()
                }
            }
        }

        viewModel.navigationEvent.observe(this) { event ->
            binding.progressCircular.isVisible = false
            binding.shimmerListContainer.root.isVisible = false
            adapter.hideLoading()
            event.getContentIfNotHandled()?.let { itemDetails ->
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("item", itemDetails)
                startActivity(intent)
            }
        }
    }
}

sealed class MainActivityUiState<out T> {
    object Loading : MainActivityUiState<Nothing>()

    object LoadingNewItems : MainActivityUiState<Nothing>()

    object LoadingMoreItems : MainActivityUiState<Nothing>()

    data class SuccessNewItems<T>(val data: T) : MainActivityUiState<T>()

    data class SuccessMoreItems<T>(val data: T) : MainActivityUiState<T>()

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
}