package com.example.melichallenge.presentation.imageslider

import android.content.Context
import android.media.Image
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.example.melichallenge.R
import com.example.melichallenge.databinding.ImageSliderViewBinding
import com.example.melichallenge.presentation.adapters.ImageSliderAdapter

class ImageSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = DataBindingUtil.inflate<ImageSliderViewBinding>(
        LayoutInflater.from(context),
        R.layout.image_slider_view,
        this,
        true
    )
    private val adapter = ImageSliderAdapter()

    init {
        binding.lifecycleOwner = (context as? LifecycleOwner)

        binding.imageSlider.adapter = adapter

        binding.imageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.isLabelVisible = adapter.itemCount > 1
                binding.imagePositionLabel = "${position + 1}/${adapter.itemCount}"
            }
        })
    }

    fun setImageUrls(urlList: List<String>) {
        adapter.submitList(urlList)
        binding.imageSlider.setCurrentItem(0, false)
    }
}