package com.weather.map.presentation.core

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Fawzy on 2021-07-16
 * ma7madfawzy@gmail.com
 */

open class BaseViewHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(
    binding.root
)
