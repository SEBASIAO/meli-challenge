package com.example.melichallenge.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melichallenge.databinding.ItemViewBinding
import com.example.melichallenge.domain.models.Item

class ItemAdapter(private val onItemClick: (Item) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val currentItems = mutableListOf<Item>()

    class ItemViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = currentItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = currentItems[position]
        holder.binding.item = item
        Glide.with(holder.binding.thumbnail.context)
            .load(item.thumbnail)
            .into(holder.binding.thumbnail)
        holder.binding.root.setOnClickListener { onItemClick.invoke(item) }
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
}