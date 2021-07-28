package com.app.weather.presentation.core

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.app.weather.utils.extensions.networkAvailable

/**
 * Created by Fawzy
 */
abstract class BaseVmFragment<VM : BaseViewModel, DB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    viewModelClass: Class<VM>
) : BaseFragment<DB>(layoutResId) {

    protected val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            setVariable(BR.viewModel, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }
        super.onViewCreated(view, savedInstanceState)
    }

    interface NetworkAvailableCallback {
        fun isNetworkAvailable(): Boolean
    }
}
