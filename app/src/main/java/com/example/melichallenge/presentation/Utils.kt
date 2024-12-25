package com.example.melichallenge.presentation

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Double.formatPrice() : String {
    val roundedValue = if (this % 1 >= 0.5) this.toInt() + 1 else this.toInt()
    return "$ ${String.format("%,d", roundedValue)}"
}