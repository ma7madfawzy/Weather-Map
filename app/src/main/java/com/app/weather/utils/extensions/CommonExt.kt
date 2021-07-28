package com.app.weather.utils.extensions

fun String.toDoubleOrZero(string: String?): Double {
    string?.let {
      return  it.toDouble()
    } ?: run {
      return 0.0
    }
}