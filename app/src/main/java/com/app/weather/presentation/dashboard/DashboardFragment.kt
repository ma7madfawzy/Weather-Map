package com.app.weather.presentation.dashboard

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.weather.R
import com.app.weather.databinding.FragmentDashboardBinding
import com.app.weather.domain.model.ListItem
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.dashboard.forecast.ForecastAdapter
import com.app.weather.presentation.main.MainActivity
import com.app.weather.utils.extensions.logE
import com.app.weather.utils.extensions.observeWith
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseVmFragment<DashboardFragmentViewModel, FragmentDashboardBinding>(
    R.layout.fragment_dashboard,
    DashboardFragmentViewModel::class.java,
) {
    override fun init() {
        super.init()
        initForecastAdapter()
        sharedElementReturnTransition(android.R.transition.move)

        observeForecastState()
        observeCurrentWeatherState()
        val args: DashboardFragmentArgs by navArgs()
        viewModel.updateParams(args.lat, args.lng).isPinned.set(args.isPinned)
        logE("lat: in args: ${args.lat}, long: ${args.lng}")
    }

    private fun observeCurrentWeatherState() {
        viewModel.currentWeatherViewState.observeWith(
            viewLifecycleOwner
        ) {
            with(binding) {
                currentWeatherViewState = it
                containerForecast.entity = it.data
                containerForecast.textViewTemperature.text = it.data?.main?.getTempString()
                invalidateAll()
            }
        }
    }

    private fun observeForecastState() {
        viewModel.forecastViewState.observeWith(viewLifecycleOwner) {
            binding.viewState = it
            it.data?.list?.let { forecasts -> updateAdapter(forecasts) }
            (activity as MainActivity).setToolbarTitle(
                it.data?.city?.getCityAndCountry()
            )
        }
    }


    private fun initForecastAdapter() {
        val adapter = ForecastAdapter { item, cardView, forecastIcon, dayOfWeek, temp, tempMaxMin ->
            findNavController()
                .navigate(
                    DashboardFragmentDirections.actionDashboardFragmentToWeatherDetailFragment(
                        item, viewModel.forecastViewState.value?.data!!
                    ),
                    getWeatherDetailsSharedElementsExtras(
                        cardView, forecastIcon, dayOfWeek,
                        temp, tempMaxMin
                    )
                )
        }
        binding.recyclerForecast.adapter = adapter
        handleEnterTransition()
    }

    private fun handleEnterTransition() {
        postponeEnterTransition()
        binding.recyclerForecast.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }


    private fun getWeatherDetailsSharedElementsExtras(
        cardView: View, forecastIcon: View, dayOfWeek: View, temp: View, tempMaxMin: View
    ) = addSharedElements(
        mapOf(
            cardView to cardView.transitionName,
            forecastIcon to forecastIcon.transitionName,
            dayOfWeek to dayOfWeek.transitionName,
            temp to temp.transitionName,
            tempMaxMin to tempMaxMin.transitionName
        )
    )


    private fun updateAdapter(list: List<ListItem>) {
        (binding.recyclerForecast.adapter as ForecastAdapter).submitList(list)
    }
}
