package com.weather.map.presentation.core

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import com.weather.map.R
import com.weather.map.utils.extensions.afterTextChanged
import com.weather.map.utils.extensions.hide
import com.weather.map.utils.extensions.show
import com.weather.map.utils.extensions.snackError
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * Created by Fawzy on 2021-07-16
 * ma7madfawzy@gmail.com
 */

@BindingAdapter("app:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.show()
    } else {
        view.hide()
    }
}

@BindingAdapter("app:setWeatherIcon")
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

@BindingAdapter("error")
fun error(view: View, error: Int) {
    if (error == 0) return
    when (view) {
        is EditText -> view.error =
            view.getContext().getString(error)
        is TextInputLayout -> {
            error(view, error)
        }
        else -> view.snackError(error)
    }
}

@BindingAdapter("error")
fun error(view: TextInputLayout, error: Int) {
    if (error == 0 || (view.tag != null && view.tag is TextWatcher)) return
    view.isErrorEnabled = true
    view.error = view.context.getString(error)
    view.editText?.let {
        it.afterTextChanged {
            view.error = null
            view.isErrorEnabled = false
            view.tag = it
        }
    }
}


@BindingAdapter("snack_error")
fun snackError(view: View, @StringRes message: Int?) {
    message?.let {
        view.snackError(it)
    }
}

@BindingAdapter("snack")
fun snackError(view: View, s: String?) {
    s?.let { view.snackError(s) }
}

@BindingAdapter("toast")
fun toast(view: View, s: String?) {
    s?.let { Toast.makeText(view.context, s, Toast.LENGTH_LONG).show() }
}


@BindingAdapter("textRes")
fun textRes(view: TextView, @StringRes res: Int?) {
    res?.let {
        view.text = view.context.getString(it)
    }
}

@BindingAdapter("setText")
fun setText(view: TextView, text: String?) {
    text?.let {
        view.text = it
    }
}

@BindingAdapter("showAlert")
fun showAlert(view: View, message: String?) {
    message?.let {
        val builder1 = AlertDialog.Builder(view.context)
        builder1.setMessage(message)
        builder1.setTitle(view.context.getString(R.string.smth_went_wrong))
        builder1.setCancelable(true)
        builder1.setPositiveButton(view.context.getString(R.string.ok), null)
        val alert11 = builder1.create()
        alert11.show()
    }
}

@BindingAdapter("showValidationAlert")
fun showValidationAlert(view: View, @StringRes message: Int?) {
    if (message == 0) return
    message?.let {
        val builder1 = AlertDialog.Builder(view.context)
        builder1.setMessage(view.context.getString(message))
        builder1.setTitle(view.context.getString(R.string.smth_went_wrong))
        builder1.setCancelable(true)
        builder1.setPositiveButton(view.context.getString(R.string.ok)) { _, _ ->
            run {
                if (view is EditText) (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

            }
        }
        val alert11 = builder1.create()
        alert11.show()
    }
}

@BindingAdapter("toolbar_title")
fun setToolbarTitle(view: Toolbar, title: String?) {
    view.title = title ?: ""
}

@BindingAdapter("hasFixedSize")
fun hasFixedSize(view: RecyclerView, value: Boolean?) {
    value?.let { view.setHasFixedSize(it) }
}

@BindingAdapter("round_corners")
fun roundCorners(imageView: ShapeableImageView, round_corners: Boolean) {
    if (!round_corners) return
    val radius = imageView.context.resources.getDimension(R.dimen.default_corner_radius)
    imageView.shapeAppearanceModel = imageView.shapeAppearanceModel
        .toBuilder()
        .setAllCorners(CornerFamily.ROUNDED, radius)
        .build()

}


@BindingAdapter("linksClickable")
fun linksClickable(view: TextView, text: String?) {
    text?.let {
        view.movementMethod = LinkMovementMethod.getInstance()
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("formatDate")
//converts date in format such as "2021-06-07T14:44:06Z"
fun formatDate(view: TextView, text: String?) {
    text?.let {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val output = SimpleDateFormat("MMM dd yyyy")

        try {
            val d = input.parse(text)
            view.text = output.format(d!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}
