package com.app.weather.presentation.home

import android.location.Location
import android.view.View
import androidx.navigation.fragment.findNavController
import com.algolia.search.saas.AbstractQuery
import com.app.weather.R
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.databinding.FragmentHomeBinding
import com.app.weather.presentation.core.BaseVmRequireLocationFragment
import com.app.weather.presentation.core.Constants
import com.app.weather.presentation.home.pinned_locations.PinnedLocationsAdapter
import com.app.weather.presentation.main.MainActivity
import com.app.weather.utils.LocationTracker
import com.app.weather.utils.extensions.observeWith
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseVmRequireLocationFragment<HomeFragmentViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeFragmentViewModel::class.java,
) {
    override fun init() {
        super.init()
        initForecastAdapter()
        sharedElementReturnTransition(android.R.transition.move)
        observeCurrentWeatherState()
        observePinnedLocationsState()
    }

    private fun updateWeatherParam(latLng: AbstractQuery.LatLng) {
        viewModel.updateWeatherParams(latLng)
    }

    override fun onStart() {
        super.onStart()
        getAdapter().clearData()
        viewModel.fetchPinnedLocations()
    }

    private fun observeCurrentWeatherState() {
        binding.viewModel?.currentWeatherViewState?.observeWith(
            viewLifecycleOwner
        ) {
            (activity as MainActivity).setToolbarTitle(it.data?.name)
            with(binding) {
                currentWeatherViewState = it
                containerForecast.entity = it.data
            }
        }
    }

    private fun observePinnedLocationsState() {
        binding.viewModel?.pinnedLocationsWeatherViewState?.observeWith(
            viewLifecycleOwner
        ) {
            it?.let { list -> updateAdapter(list) }
        }
    }

    private fun initForecastAdapter() {
        val adapter = PinnedLocationsAdapter { entity, pos, card, temp ->
            findNavController()
                .navigate(
                    HomeFragmentDirections.actionHomeFragmentToDashboardFragment(
                        true,
                        viewModel.getLatByPosition(pos), viewModel.getLngByPosition(pos)
                    ),
                    getWeatherDetailsSharedElementsExtras(card, temp)
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

    private fun getWeatherDetailsSharedElementsExtras(cardView: View, forecastIcon: View) =
        addSharedElements(
            mapOf(
                cardView to cardView.transitionName,
                forecastIcon to forecastIcon.transitionName,
            )
        )

    private fun updateAdapter(list: List<CurrentWeatherEntity>) {
        getAdapter().submitList(list)
    }

    private fun getAdapter() = (binding.recyclerForecast.adapter as PinnedLocationsAdapter)
    override fun onLocationResult(obj: LocationTracker?, location: Location) {
        updateWeatherParam(AbstractQuery.LatLng(location.latitude, location.longitude))
    }

    override fun onLocationError(obj: LocationTracker?, errorCode: Int, msg: String?) {
        setToLondon()
    }

    private fun setToLondon() {
        updateWeatherParam(
            AbstractQuery.LatLng(
                Constants.DataStore.DEFAULT_LAT,
                Constants.DataStore.DEFAULT_LONG
            )
        )
    }
}
