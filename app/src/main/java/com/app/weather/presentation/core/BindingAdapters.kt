package com.app.weather.presentation.core

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weather.R
import com.app.weather.utils.extensions.hide
import com.app.weather.utils.extensions.hideKeyboard
import com.app.weather.utils.extensions.show
import com.app.weather.utils.extensions.showKeyboardOnFocusChanged
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

@BindingAdapter("setDisplayHomeAsUpEnabled")
fun setDisplayHomeAsUpEnabled(view: View, display: Boolean) {
    if (view.context is AppCompatActivity)
        (view.context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(display)
}

@BindingAdapter("setNavigationIconTint")
fun setNavigationIconTint(toolbar: Toolbar, color: Int) {
    toolbar.navigationIcon?.setTint(color)
}

@BindingAdapter("tint")
fun tint(imageView: ImageView, color: Int) {
    imageView.setColorFilter(color)
}

@BindingAdapter("setNavigationIcon")
fun setNavigationIcon(toolbar: Toolbar, @DrawableRes drawable: Int?) {
    toolbar.setNavigationIcon(drawable ?: 0)
}

@BindingAdapter("setLayoutManager")
fun setLayoutManager(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.layoutManager = LinearLayoutManager(
        recyclerView.context,
        orientation,
        false
    )
}

@BindingAdapter("searchViewEditTextColor")
fun searchViewEditTextColor(view: SearchView, color: Int) {
    val searchEditText: EditText = view.findViewById(R.id.search_src_text)
    view.context?.let { searchEditText.setTextColor(color) }
}

@BindingAdapter("searchViewEditHintColor")
fun searchViewEditHintColor(view: SearchView, color: Int) {
    val searchEditText: EditText = view.findViewById(R.id.search_src_text)
    view.context?.let { searchEditText.setHintTextColor(color) }
}

@BindingAdapter("setIconifiedByDefault")
fun setIconifiedByDefault(view: SearchView, value: Boolean) {
    view.setIconifiedByDefault(value)
    view.isIconified = value
}

@BindingAdapter("setIsSearchViewActivated")
fun setIsSearchViewActivated(view: SearchView, value: Boolean) {
    view.isActivated = value
}

@BindingAdapter("showKeyboard")
fun showKeyboard(view: SearchView, show: Boolean) {
    if (show) view.showKeyboardOnFocusChanged()
    else view.hideKeyboard()
}
/**
 * App Use Cases
 * which changes from one app to another based on logic but the above serve general purpose
 * */
