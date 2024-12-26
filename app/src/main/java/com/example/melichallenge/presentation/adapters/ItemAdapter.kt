package com.example.melichallenge.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melichallenge.databinding.ItemViewBinding
import com.example.melichallenge.databinding.LoadingItemBinding
import com.example.melichallenge.domain.models.Item

class ItemAdapter(private val onItemClick: (Item) -> Unit) :
    RecyclerView.Adapter<BaseItemViewHolder>() {

    private val currentItems = mutableListOf<Item>()
    private var mShowLoading = false

    companion object {
        private const val VIEW_TYPE_LOADING = 1
        private const val VIEW_TYPE_NORMAL = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == VIEW_TYPE_LOADING) {
            val binding = LoadingItemBinding.inflate(layoutInflater, parent, false)
            return LoadingItemViewHolder(binding)
        } else {
            val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return currentItems.size + if (mShowLoading) 1 else 0
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = currentItems[position]
            holder.bind(item, onItemClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < currentItems.size
        ) {
            return VIEW_TYPE_NORMAL
        }
        return VIEW_TYPE_LOADING
    }

    fun appendList(newItems: List<Item>) {
        val startPosition = currentItems.size
        currentItems.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun setNewItems(newItems: List<Item>) {
        currentItems.clear()
        currentItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun hideLoading() {
        if (mShowLoading) {
            mShowLoading = false
            notifyItemRemoved(currentItems.size)
        }
    }

    fun showLoading() {
        if (!mShowLoading) {
            mShowLoading = true
            notifyItemInserted(currentItems.size)
        }
    }
}

abstract class BaseItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Item, onItemClick: (Item) -> Unit)
}

class ItemViewHolder(private val binding: ItemViewBinding) : BaseItemViewHolder(binding.root) {
    override fun bind(item: Item, onItemClick: (Item) -> Unit) {
        binding.item = item
        Glide.with(binding.thumbnail.context)
            .load(item.thumbnail)
            .into(binding.thumbnail)
        binding.root.setOnClickListener { onItemClick.invoke(item) }
    }
}

class LoadingItemViewHolder(binding: LoadingItemBinding) : BaseItemViewHolder(binding.root) {
    override fun bind(item: Item, onItemClick: (Item) -> Unit) = Unit
}

