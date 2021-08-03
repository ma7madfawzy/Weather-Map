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
import com.app.weather.utils.extensions.observeWith
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseVmFragment<DashboardFragmentViewModel, FragmentDashboardBinding>(
    R.layout.fragment_dashboard,
    DashboardFragmentViewModel::class.java,
) {
    override fun init() {
        super.init()
        binding.viewModel = viewModel
        initForecastAdapter()
        sharedElementReturnTransition(android.R.transition.slide_right)

        observeForecastState()
        val args: DashboardFragmentArgs by navArgs()
        viewModel.updateParams(args.lat, args.lng).isPinned.set(args.isPinned)
        initCardClickListener()
    }

    private fun initCardClickListener() {
        binding.container.cardView.setOnClickListener { _ ->
            viewModel.forecastViewState.value?.data?.list?.get(0)?.let { navigateToDetails(it) }
        }
    }


    private fun observeForecastState() {
        viewModel.forecastViewState.observeWith(viewLifecycleOwner) {
            binding.viewState = it
            binding.container.item = it.data?.list?.get(0)
            updateAdapter(it.data?.list?.subList(1, it.data.list!!.size))
            (activity as MainActivity).setToolbarTitle(
                it.data?.city?.getCityAndCountry()
            )
        }
    }


    private fun initForecastAdapter() {
        val adapter = ForecastAdapter { navigateToDetails(it) }
        binding.recyclerForecast.adapter = adapter
        handleEnterTransition()
    }

    private fun navigateToDetails(item: ListItem) {
        findNavController()
            .navigate(
                DashboardFragmentDirections.actionDashboardFragmentToWeatherDetailFragment(
                    item, viewModel.forecastViewState.value?.data!!
                )
            )
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


    private fun updateAdapter(list: List<ListItem>?) {
        (binding.recyclerForecast.adapter as ForecastAdapter).submitList(list)
    }
}
