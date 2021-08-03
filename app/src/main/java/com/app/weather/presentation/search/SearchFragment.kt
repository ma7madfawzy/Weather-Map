package com.app.weather.presentation.search

import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weather.R
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.databinding.FragmentSearchBinding
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.dashboard.DashboardFragmentDirections
import com.app.weather.presentation.main.MainActivity
import com.app.weather.presentation.search.result.SearchResultAdapter
import com.app.weather.utils.extensions.hideKeyboard
import com.app.weather.utils.extensions.logE
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
        viewModel.viewState.observeWith(
            viewLifecycleOwner
        ) {
            binding.viewState = it
            it.data?.let { results -> initSearchResultsRecyclerView(results) }
        }
    }

    private fun initSearchView() {
        val searchViewSearchIcon = binding.searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchViewSearchIcon.setImageResource(R.drawable.ic_search)
        val queryTextChange: (newText: String) -> Unit = { viewModel.onTextChange(it) }
        binding.searchView.queryTextListener(
            onQueryTextSubmit = queryTextChange,
            onQueryTextChange = queryTextChange
        )
    }

    private fun initSearchResultsAdapter() {
        val adapter = SearchResultAdapter { item ->
            item.coord?.let {
                binding.searchView.hideKeyboard()
                findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToDashboardFragment
                        (false,it.lat?:0.0,it.lon?:0.0))
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
