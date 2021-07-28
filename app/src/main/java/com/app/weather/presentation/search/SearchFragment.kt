package com.app.weather.presentation.search

import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weather.R
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.databinding.FragmentSearchBinding
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.main.MainActivity
import com.app.weather.presentation.search.result.SearchResultAdapter
import com.app.weather.utils.extensions.hideKeyboard
import com.app.weather.utils.extensions.observeWith
import com.app.weather.utils.extensions.queryTextListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseVmFragment<SearchViewModel, FragmentSearchBinding>(
    R.layout.fragment_search,
    SearchViewModel::class.java,
) {
    override fun init() {
        super.init()
        initSearchResultsAdapter()
        initSearchView()
        observeSearchViewStateData()
    }

    private fun observeSearchViewStateData() {
        binding.viewModel?.viewState?.observeWith(
            viewLifecycleOwner
        ) {
            binding.viewState = it
            it.data?.let { results -> initSearchResultsRecyclerView(results) }
        }
    }

    private fun initSearchView() {
        val searchViewSearchIcon = binding.searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchViewSearchIcon.setImageResource(R.drawable.ic_search)

        binding.searchView.queryTextListener({ binding.viewModel?.onTextChange(it) },
            { binding.viewModel?.onTextChange(it) })
    }

    private fun initSearchResultsAdapter() {
        val adapter = SearchResultAdapter { item ->
            item.coord?.let {
                binding.viewModel?.saveCoordinates(it)
                binding.searchView.hideKeyboard((activity as MainActivity))
                findNavController().navigate(R.id.action_searchFragment_to_dashboardFragment)
            }
        }

        binding.recyclerViewSearchResults.adapter = adapter
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun initSearchResultsRecyclerView(list: List<CitiesForSearchEntity>) {
        (binding.recyclerViewSearchResults.adapter as SearchResultAdapter).submitList(
            list.distinctBy { it.getFullName() }.sortedBy { it.importance }
        )
    }
}
