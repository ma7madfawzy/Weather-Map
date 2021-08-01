package com.app.weather.presentation.home

import android.location.Location
import android.view.View
import androidx.navigation.Navigator
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
    private lateinit var currentLocation: AbstractQuery.LatLng

    override fun init() {
        super.init()
        initForecastAdapter()
        sharedElementReturnTransition(android.R.transition.slide_bottom)
        observeCurrentWeatherState()
        observePinnedLocationsState()
        setAddLocationsToHomeClickListener()
        setCurrentForecastCastClickListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.requestPinnedLocations(true)
        getAdapter().clearData()
    }

    override fun onLocationResult(obj: LocationTracker?, location: Location) {
        updateWeatherParam(AbstractQuery.LatLng(location.latitude, location.longitude))
        stopReceivingUpdates()
    }

    private fun observeCurrentWeatherState() {
        viewModel.currentWeatherViewState.observeWith(
            viewLifecycleOwner
        ) {
            (activity as MainActivity).setToolbarTitle(it.data?.name)
            with(binding) {
                containerForecast.entity = it.data
            }
        }
    }

    private fun observePinnedLocationsState() {
        viewModel.pinnedLocationsWeatherViewState.observeWith(
            viewLifecycleOwner
        ) {
            it?.let { list -> updateAdapter(list) }
        }
    }

    private fun initForecastAdapter() {
        val adapter = PinnedLocationsAdapter { item, card, temp ->
            navigateToDashboard(
                true, item.lat!!,
                item.lng!!,
                getWeatherDetailsSharedElementsExtras(card, temp)
            )
        }
        binding.recyclerForecast.adapter = adapter
        handleEnterTransition()
    }
    private fun setAddLocationsToHomeClickListener() {
        binding.chipAdd.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }

    private fun updateWeatherParam(latLng: AbstractQuery.LatLng) {
        currentLocation = latLng
        viewModel.updateWeatherParams(latLng)
    }

    private fun setCurrentForecastCastClickListener() {
        binding.containerForecast.cardView.setOnClickListener {
            navigateToDashboard(
                false, currentLocation.lat, currentLocation.lng,
                getWeatherDetailsSharedElementsExtras(
                    binding.containerForecast.cardView,
                    binding.containerForecast.textViewTemperature
                )
            )
        }
    }

    private fun navigateToDashboard(
        isPinned: Boolean,
        lat: Double,
        lng: Double,
        extras: Navigator.Extras
    ) {
        findNavController()
            .navigate(
                HomeFragmentDirections.actionHomeFragmentToDashboardFragment(
                    isPinned, lat, lng
                ), extras
            )
    }

    override fun onLocationError(obj: LocationTracker?, errorCode: Int, msg: String?) {
        setToLondon()
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

    private fun setToLondon() {
        updateWeatherParam(
            AbstractQuery.LatLng(
                Constants.DataStore.DEFAULT_LAT,
                Constants.DataStore.DEFAULT_LONG
            )
        )
    }
}
