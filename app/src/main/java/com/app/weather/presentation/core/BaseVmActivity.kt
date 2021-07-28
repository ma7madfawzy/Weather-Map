package com.app.weather.presentation.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Fawzy
 */
abstract class BaseVmActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    viewModelClass: Class<VM>
) : BaseActivity<DB>(layoutResId){

    val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            setVariable(BR.viewModel, viewModel)
            lifecycleOwner = this@BaseVmActivity
        }
    }
}
