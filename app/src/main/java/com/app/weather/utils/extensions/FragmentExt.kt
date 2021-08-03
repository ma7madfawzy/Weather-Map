package com.app.weather.utils.extensions

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

fun androidx.fragment.app.Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = activity?.toast(
    message,
    duration
)

inline fun androidx.fragment.app.Fragment.alertDialog(
    body: AlertDialog.Builder.() -> AlertDialog.Builder
) = activity?.alertDialog(body)

inline fun Fragment.addOnFragmentDestroyedObserver(action: Runnable): Boolean {
    return try {
        val fragmentLifecycleEventObserver = arrayOfNulls<LifecycleEventObserver>(1)
        val action2 = arrayOf<Runnable?>(action)
        val observer =
            LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    if (fragmentLifecycleEventObserver[0] != null) source.lifecycle.removeObserver(
                        fragmentLifecycleEventObserver[0]!!
                    )
                    fragmentLifecycleEventObserver[0] = null
                    action2[0]!!.run()
                    action2[0] = null
                }
            }
        fragmentLifecycleEventObserver[0] = observer
        lifecycle.addObserver(observer)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


