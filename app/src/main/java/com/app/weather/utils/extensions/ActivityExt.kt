package com.app.weather.utils.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

inline fun Activity.alertDialog(body: AlertDialog.Builder.() -> AlertDialog.Builder): AlertDialog {
    return AlertDialog.Builder(this)
        .body()
        .show()
}
 fun Activity.addOnActivityDestroyed(action: Runnable) {
    val callback = arrayOfNulls<Application.ActivityLifecycleCallbacks>(1)
    val action2 = arrayOf<Runnable?>(action)
    val className = javaClass.name
    callback[0] = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            if (className == activity.javaClass.name) {
                activity.application.unregisterActivityLifecycleCallbacks(callback[0])
                callback[0] = null
                action2[0]!!.run()
                action2[0] = null
            }
        }
    }
    application?.registerActivityLifecycleCallbacks(callback[0])
}
