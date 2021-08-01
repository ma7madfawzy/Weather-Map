package com.app.weather.presentation.weather_detail

import android.transition.TransitionInflater
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.weather.R
import com.app.weather.databinding.FragmentWeatherDetailBinding
import com.app.weather.domain.model.ListItem
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.weather_detail.weatherHourOfDay.WeatherHourOfDayAdapter
import com.app.weather.utils.extensions.observeWith
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailFragment : BaseVmFragment<WeatherDetailViewModel, FragmentWeatherDetailBinding>(
    R.layout.fragment_weather_detail,
    WeatherDetailViewModel::class.java,
) {

    override fun init() {
        super.init()
        sharedElementReturnTransition(android.R.transition.slide_bottom)

        val args: WeatherDetailFragmentArgs by navArgs()
        viewModel.initWithArgs(args)

        observeSelectedDayForecast()

        binding.fabClose.setOnClickListener { findNavController().popBackStack() }

    }

    private fun observeSelectedDayForecast() {
        viewModel.selectedDayForecastLiveData.observeWith(viewLifecycleOwner)
        { initWeatherHourOfDayAdapter(it) }
    }

    private fun initWeatherHourOfDayAdapter(list: List<ListItem>) {
        val adapter = WeatherHourOfDayAdapter()
        binding.recyclerViewHourOfDay.adapter = adapter
        (binding.recyclerViewHourOfDay.adapter as WeatherHourOfDayAdapter).submitList(list)
    }
}
