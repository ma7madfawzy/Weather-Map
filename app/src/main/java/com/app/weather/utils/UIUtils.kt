package com.app.weather.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class UIUtils private constructor() {
    fun addOnFragmentDestroyedObserver(fragment: Fragment, action: Runnable): Boolean {
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
            fragment.lifecycle.addObserver(observer)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun addOnActivityDestroyed(activity: Activity, action: Runnable) {
        val callback = arrayOfNulls<ActivityLifecycleCallbacks>(1)
        val action2 = arrayOf<Runnable?>(action)
        val className = activity.javaClass.name
        callback[0] = object : ActivityLifecycleCallbacks {
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
        activity.application.registerActivityLifecycleCallbacks(callback[0])
    }

    companion object {
        fun createInstance(): UIUtils {
            return UIUtils()
        }
    }
}