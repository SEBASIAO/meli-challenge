package com.example.melichallenge.presentation

import android.os.Bundle
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
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        setUpItemsRecyclerView()
        viewModel.searchItemsByQuery("Iphone")
    }

    private fun setUpItemsRecyclerView(){
        val adapter = ItemAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = layoutManager

        binding.searchRv.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = viewModel.isLoading

        })

        viewModel.items.observe(this) { items ->
            adapter.appendList(items)
        }
    }
}