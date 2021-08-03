package com.app.weather.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.weather.presentation.core.BaseAdapter
import com.app.weather.databinding.ItemForecastBinding
import com.app.weather.domain.model.ListItem

/**
 * Created by Fawzy
 */

class ForecastAdapter(
    private val callBack: (ListItem?) -> Unit
) : BaseAdapter<ListItem,ItemForecastBinding>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemForecastBinding {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.cardView.setOnClickListener { callBack(binding.item) }
        return binding
    }

    override fun bind(binding: ItemForecastBinding, position: Int) {
        binding.item=getItem(position)
        binding.executePendingBindings()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<ListItem>() {
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.dtTxt == newItem.dtTxt
}
