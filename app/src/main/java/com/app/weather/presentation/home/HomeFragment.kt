package com.app.weather.presentation.home

import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.weather.R
import com.app.weather.databinding.FragmentHomeBinding
import com.app.weather.domain.model.ListItem
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.app.weather.presentation.dashboard.DashboardFragmentDirections
import com.app.weather.presentation.dashboard.forecast.ForecastAdapter
import com.app.weather.presentation.main.MainActivity
import com.app.weather.utils.extensions.logE
import com.app.weather.utils.extensions.observeWith
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseVmFragment<HomeFragmentViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeFragmentViewModel::class.java,
) {

    override fun init() {
        super.init()
        initForecastAdapter()
        sharedElementReturnTransition(android.R.transition.move)
        viewModel.updateWeatherParams(
            LatLng(
                Constants.DataStore.DEFAULT_LAT,
                Constants.DataStore.DEFAULT_LONG
            )
        )
        observeCurrentWeatherState()
        observePinnedLocationsState()
    }


    private fun observeCurrentWeatherState() {
        binding.viewModel?.currentWeatherViewState?.observeWith(
            viewLifecycleOwner
        ) {
            (activity as MainActivity).setToolbarTitle(it.data?.name)
            with(binding) {
                currentWeatherViewState = it
                containerForecast.viewState = it
            }
        }
    }

    private fun observePinnedLocationsState() {
        binding.viewModel?.pinnedLocationsWeatherViewState?.observeWith(
            viewLifecycleOwner
        ) {
            logE(it.toString())
        }
    }

    private fun initForecastAdapter() {
        val adapter = ForecastAdapter { item, cardView, forecastIcon, dayOfWeek, temp, tempMaxMin ->
            findNavController()
                .navigate(
                    getActionDashboardFragmentToWeatherDetailFragment(item),
                    getWeatherDetailsSharedElementsExtras(
                        cardView,
                        forecastIcon,
                        dayOfWeek,
                        temp,
                        tempMaxMin
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

    private fun getActionDashboardFragmentToWeatherDetailFragment(item: ListItem) =
        DashboardFragmentDirections.actionDashboardFragmentToWeatherDetailFragment(item)

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
