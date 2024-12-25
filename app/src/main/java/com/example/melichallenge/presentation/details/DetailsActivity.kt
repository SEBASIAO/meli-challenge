package com.example.melichallenge.presentation.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.melichallenge.databinding.ActivityDetailsBinding
import com.example.melichallenge.domain.models.Item

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val item = intent.getParcelableExtra("item", Item::class.java)

        item?.let {
            binding.item = item
            item.pictures?.let {
                binding.itemImageSlider.setImageUrls(it.map { itemPicture -> itemPicture.url.orEmpty() })
            }
        }
    }
}