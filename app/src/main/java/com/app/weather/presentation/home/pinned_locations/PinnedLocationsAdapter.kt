package com.app.weather.presentation.home.pinned_locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.databinding.ItemPinnedLocationBinding
import com.app.weather.presentation.core.BaseAdapter

/**
 * Created by Fawzy
 */

class PinnedLocationsAdapter(
    private val callBack: (CurrentWeatherEntity, Int, View, View) -> Unit
) : BaseAdapter<CurrentWeatherEntity, ItemPinnedLocationBinding>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int) =
        ItemPinnedLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


    override fun bind(binding: ItemPinnedLocationBinding, position: Int) {
        binding.entity = getItem(position)
        binding.cardView.setOnClickListener {
            callBack(
                getItem(position), position, binding.cardView,
                binding.textViewTemp
            )
        }
        binding.executePendingBindings()
    }

    fun clearData() {
        currentList.clear()
        notifyDataSetChanged()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<CurrentWeatherEntity>() {
    override fun areContentsTheSame(
        oldItem: CurrentWeatherEntity,
        newItem: CurrentWeatherEntity
    ): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(
        oldItem: CurrentWeatherEntity,
        newItem: CurrentWeatherEntity
    ): Boolean =
        oldItem.id == newItem.id
}
