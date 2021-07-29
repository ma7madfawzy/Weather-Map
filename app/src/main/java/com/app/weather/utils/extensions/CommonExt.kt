package com.app.weather.utils.extensions

fun <T> ArrayList<T>.addItem(t:T): ArrayList<T> {
    add(t)
    return this
}