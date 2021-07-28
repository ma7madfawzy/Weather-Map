package com.app.weather.presentation.core

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weather.R
import com.app.weather.utils.extensions.hide
import com.app.weather.utils.extensions.show
import com.squareup.picasso.Picasso

/**
 * Created by Fawzy
 */

@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.show()
    } else {
        view.hide()
    }
}

@BindingAdapter("hide")
fun setHidden(view: View, hide: Boolean) {
    setVisibility(view, !hide)
}

@BindingAdapter("setWeatherIcon")
fun setWeatherIcon(view: ImageView, iconPath: String?) {
    if (iconPath.isNullOrEmpty()) {
        return
    }
    Picasso.get().cancelRequest(view)
    val newPath = iconPath.replace(iconPath, "a$iconPath")
    val imageid =
        view.context.resources.getIdentifier(newPath + "_svg", "drawable", view.context.packageName)
    val imageDrawable = ResourcesCompat.getDrawable(view.context.resources, imageid, null)
    view.setImageDrawable(imageDrawable)
}

@BindingAdapter("setErrorView")
fun setErrorView(view: View, viewState: BaseViewState?) {
    if (viewState?.shouldShowErrorMessage() == true) {
        view.show()
    } else {
        view.hide()
    }

    view.setOnClickListener { view.hide() }
}

@BindingAdapter("setErrorText")
fun setErrorText(view: TextView, viewState: BaseViewState?) {
    if (viewState?.shouldShowErrorMessage() == true) {
        view.text = viewState.getErrorMessage()
    } else {
        view.text = view.context.getString(R.string.unexpected_exception)
    }
}

@BindingAdapter("setDisplayHomeAsUpEnabled")
fun setDisplayHomeAsUpEnabled(view: View, display: Boolean) {
    if (view.context is AppCompatActivity)
        (view.context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(display)
}

@BindingAdapter("setNavigationIconTint")
fun setNavigationIconTint(toolbar: Toolbar, color: Int) {
    toolbar.navigationIcon?.setTint(color)
}

@BindingAdapter("setNavigationIcon")
fun setNavigationIcon(toolbar: Toolbar, drawable: Int?) {
    toolbar.setNavigationIcon(drawable ?: 0)
}
@BindingAdapter("setLayoutManager")
fun setLayoutManager(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.layoutManager = LinearLayoutManager(
        recyclerView.context,
       orientation ,
        false
    )
}

/**
 * App Use Cases
 * which changes from one app to another based on logic but the above serve general purpose
 * */
