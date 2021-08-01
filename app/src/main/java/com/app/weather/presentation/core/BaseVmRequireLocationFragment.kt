package com.app.weather.presentation.core

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import com.app.weather.R
import com.app.weather.utils.LocationTracker


/**
 * Created by Fawzy
 */
abstract class BaseVmRequireLocationFragment<VM : BaseViewModel, DB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    viewModelClass: Class<VM>
) : BaseVmFragment<VM, DB>(layoutResId, viewModelClass) {

    private var requiredPermissionGranted = false
    private var mLocationTracker: LocationTracker? = null

    override fun onStart() {
        super.onStart()
        checkRequiredPermissions()
    }

    private fun checkRequiredPermissions() {
        val accessFineLocationGranted = ActivityCompat.checkSelfPermission(
            requireActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val accessCoarseLocationGranted = ActivityCompat.checkSelfPermission(
            requireActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!accessFineLocationGranted || !accessCoarseLocationGranted) {
            requiredPermissionGranted = false
            requestMultiplePermissions.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        } else {
            requiredPermissionGranted = true
            setupLocationTracker()
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[ACCESS_FINE_LOCATION] == true && permissions[ACCESS_COARSE_LOCATION] == true) {
                requiredPermissionGranted = true
                setupLocationTracker()
            } else checkRequiredPermissions()
        }

    //----------------------------------------------------------------------------------------------
    private fun showEnableGPSAlert() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.apply {
            setTitle(R.string.requireGps)
            setPositiveButton(R.string.ok) { dialogInterface, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialogInterface.dismiss()
            }
            setNegativeButton(R.string.negative) { _, _ -> }
        }.create().show()
    }

    abstract fun onLocationResult(obj: LocationTracker?, location: Location)
    abstract fun onLocationError(obj: LocationTracker?, errorCode: Int, msg: String?)
    override fun onDestroy() {
        super.onDestroy()
        stopReceivingUpdates()
    }

    fun stopReceivingUpdates() {
        mLocationTracker?.stopLocationUpdating()
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationTracker() {
        if (!requiredPermissionGranted) return
        mLocationTracker = LocationTracker(requireActivity(), object : LocationTracker.Listener {
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

}
