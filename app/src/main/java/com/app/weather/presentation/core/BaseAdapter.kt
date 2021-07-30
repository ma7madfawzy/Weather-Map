package com.app.weather.presentation.core

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.app.weather.R

/**
 * Created by Fawzy
 */

abstract class BaseAdapter<T, binding : ViewDataBinding>(callback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseViewHolder<binding>>(
        callback
    ) {

    override fun onBindViewHolder(holder: BaseViewHolder<binding>, position: Int) {
        (holder as BaseViewHolder<*>).binding.root.setTag(R.string.position, position)
        bind(holder.binding, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = getViewHolder(
        parent,
        viewType
    )

    open fun getViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(
        createBinding(parent, viewType)
    )

    abstract fun createBinding(parent: ViewGroup, viewType: Int): binding

    protected abstract fun bind(binding: binding, position: Int)
}
