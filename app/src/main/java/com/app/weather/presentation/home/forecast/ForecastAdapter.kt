package com.app.weather.presentation.home.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.app.weather.presentation.core.BaseAdapter
import com.app.weather.databinding.ItemForecastBinding
import com.app.weather.domain.model.ListItem

/**
 * Created by Fawzy
 */

class ForecastAdapter(
    private val callBack: (ListItem, View, View, View, View, View) -> Unit
) : BaseAdapter<ListItem>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewModel = ForecastItemViewModel()
//TODO        mBinding.viewModel = viewModel

        mBinding.cardView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack(
                    it,
                    mBinding.cardView,
                    mBinding.imageViewForecastIcon,
                    mBinding.textViewDayOfWeek,
                    mBinding.textViewTemp,
                    mBinding.linearLayoutTempMaxMin
                )
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemForecastBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<ListItem>() {
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.dtTxt == newItem.dtTxt
}
