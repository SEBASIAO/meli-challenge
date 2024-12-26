package com.example.melichallenge.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melichallenge.databinding.ActivityMainBinding
import com.example.melichallenge.presentation.adapters.ItemAdapter
import com.example.melichallenge.presentation.adapters.PaginationScrollListener
import com.example.melichallenge.presentation.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ItemAdapter { itemClicked ->
        viewModel.getItemDetails(itemClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        setUpItemsRecyclerView()
        setUpSearchInput()
        observeViewModel()
    }

    private fun setUpSearchInput() {
        binding.searchTf.setOnEditorActionListener { textView, actionId, _ ->
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
        val layoutManager = LinearLayoutManager(this)
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = layoutManager

        binding.searchRv.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = viewModel.isLoadingMoreItems.value
        })

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentItems.collect { items ->
                    adapter.setNewItems(items)
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MainActivityUiState.Error -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.hideLoading()
                            Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG)
                                .show()
                        }

                        is MainActivityUiState.SuccessMoreItems -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.hideLoading()
                        }

                        is MainActivityUiState.SuccessNewItems -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.hideLoading()
                        }

                        MainActivityUiState.LoadingMoreItems -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.showLoading()
                        }

                        MainActivityUiState.LoadingNewItems -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = true
                            adapter.hideLoading()
                        }

                        MainActivityUiState.Loading -> {
                            binding.progressCircular.isVisible = true
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.hideLoading()
                        }

                        MainActivityUiState.Idle -> {
                            binding.progressCircular.isVisible = false
                            binding.shimmerListContainer.root.isVisible = false
                            adapter.hideLoading()
                        }
                    }
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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

sealed class MainActivityUiState<out T> {
    object Idle : MainActivityUiState<Nothing>()

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