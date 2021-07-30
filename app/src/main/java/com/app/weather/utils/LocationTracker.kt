package com.app.weather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.weather.R

class LocationTracker : LocationListener {
    private var locationManager // Declaring a Location Manager
            : LocationManager?

    //----------------------------------------------------------------------------------------------
    var location: Location? = null
        private set
    private var mListener: Listener?

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    constructor(fragment: Fragment, callback: Listener?) {
        locationManager =
            fragment.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = callback
        UIUtils.createInstance().addOnFragmentDestroyedObserver(fragment) { destroy() }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    constructor(activity: Activity, callback: Listener?) {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = callback
        UIUtils.createInstance().addOnActivityDestroyed(activity) { destroy() }
    }

    //----------------------------------------------------------------------------------------------
    val isGPSEnabled: Boolean?
        get() = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

    //----------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    fun startLocationUpdating() {
        try {
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled!!) {
                stopLocationUpdating()
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) location =
                    locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (mListener != null && location != null) mListener!!.onLocationFounded(
                    this,
                    location!!
                )
            } else if (mListener != null) mListener!!.onErrorHappened(
                this,
                Listener.ERR_GPS_CLOSED,
                ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (mListener != null) {
                mListener!!.onErrorHappened(this, Listener.ERR_EXCEPTION, e.message)
            }
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    @SuppressLint("MissingPermission")
    fun stopLocationUpdating() {
        locationManager?.removeUpdates(this@LocationTracker)
    }

    fun getLocationDescription(context: Context?): Address? {
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder.getFromLocation(
                location!!.latitude, location!!.longitude, 1
            )
            return addresses[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     */
    //----------------------------------------------------------------------------------------------
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     */
    @JvmOverloads
    fun showGPSDisabledAlert(
        context: Context, title: String? = context.getString(R.string.gps),
        message: String? = context.getString(R.string.enableGps),
        mainButtonTitle: String? = context.getString(R.string.location_settings),
        cancelButtonTitle: String? = context.getString(R.string.negative)
    ) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(mainButtonTitle) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                Handler(Looper.getMainLooper())
                    .postDelayed(Runnable { startLocationUpdating() }, 10000)
            }
            setNegativeButton(cancelButtonTitle) { _, _ -> }
            setCancelable(false)
        }.create().show()
    }

    //----------------------------------------------------------------------------------------------
    fun destroy() {
        stopLocationUpdating()
        mListener = null
        locationManager = null
    }

    val isDestroyed: Boolean
        get() = locationManager == null

    //----------------------------------------------------------------------------------------------
    override fun onLocationChanged(location: Location) {
        if (location != null) {
            this.location = location
            if (mListener != null) {
                mListener!!.onLocationFounded(this, location)
            }
        }
    }

    override fun onProviderDisabled(provider: String) {
        if (mListener != null) mListener!!.onLocationProviderStatusChanged(this, provider, true)
    }

    override fun onProviderEnabled(provider: String) {
        if (mListener != null) mListener!!.onLocationProviderStatusChanged(this, provider, false)
    }

    @Deprecated("")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    //----------------------------------------------------------------------------------------------
    interface Listener {
        fun onLocationFounded(obj: LocationTracker?, location: Location)

        /**
         * @param provider [LocationManager.GPS_PROVIDER] or [LocationManager.NETWORK_PROVIDER]
         */
        fun onLocationProviderStatusChanged(
            obj: LocationTracker?,
            provider: String?,
            disabled: Boolean
        )

        /**
         * @param errorCode [.ERR_GPS_CLOSED] or [.ERR_EXCEPTION]
         */
        fun onErrorHappened(obj: LocationTracker?, errorCode: Int, msg: String?)

        companion object {
            const val ERR_GPS_CLOSED = 1
            const val ERR_EXCEPTION = 2
        }
    }

    companion object {
        // The minimum distance to change Updates in meters
        var MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10

        // The minimum time between updates in milliseconds
        var MIN_TIME_BW_UPDATES: Long = 1000
    }
}