package com.app.weather.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.app.weather.R

class LocationPermissionHandler(
    private val fragment: Fragment,
    private val onLocationResult: (obj: LocationTracker?, location: Location) -> Unit,
    private val onLocationError: (obj: LocationTracker?, errorCode: Int, msg: String?) -> Unit
) {
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private var requiredPermissionGranted = false
    private var mLocationTracker: LocationTracker? = null


    fun checkRequiredPermissions() {
        val accessFineLocationGranted = ActivityCompat.checkSelfPermission(
            fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val accessCoarseLocationGranted = ActivityCompat.checkSelfPermission(
            fragment.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!accessFineLocationGranted || !accessCoarseLocationGranted) {
            requiredPermissionGranted = false
            initPermissionHandler()
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            requiredPermissionGranted = true
            setupLocationTracker()
        }
    }

    private fun initPermissionHandler() {
        requestMultiplePermissions =
            fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    requiredPermissionGranted = true
                    setupLocationTracker()
                } else checkRequiredPermissions()
            }
    }

    private fun showEnableGPSAlert() {
        val alertDialog = AlertDialog.Builder(fragment.requireContext())
        alertDialog.apply {
            setTitle(R.string.requireGps)
            setPositiveButton(R.string.ok) { dialogInterface, _ ->
                fragment.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialogInterface.dismiss()
            }
            setNegativeButton(R.string.negative) { _, _ -> }
        }.create().show()
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationTracker() {
        if (!requiredPermissionGranted) return
        mLocationTracker = LocationTracker(fragment, object : LocationTracker.Listener {
            override fun onLocationFounded(obj: LocationTracker?, location: Location) {
                onLocationResult(obj, location)
            }

            override fun onLocationProviderStatusChanged(
                p0: LocationTracker?, p1: String?,
                disabled: Boolean
            ) {
                if (disabled) if (LocationManager.GPS_PROVIDER == p1) showEnableGPSAlert()
            }

            override fun onErrorHappened(obj: LocationTracker?, errorCode: Int, msg: String?) {
                onLocationError(obj, errorCode, msg)
            }
        })
        mLocationTracker?.startLocationUpdating()
    }

    fun unsubscribe() {
        mLocationTracker?.stopLocationUpdating()
    }

}