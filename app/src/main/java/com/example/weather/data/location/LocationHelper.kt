package com.example.weather.data.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

/**
 * Helper class to manage location updates and permissions
 */
class LocationHelper(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>,
    private val locationCallback: (Location) -> Unit,
) {
    private var locationManager: LocationManager? = null

    /**
     * Checks if the location permission is granted. If not, it requests the permission.
     */
    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Stops location updates by removing the GPS location listener
     */
    fun stopLocationUpdates() {
        locationManager?.let { manager ->
            manager.removeUpdates(gpsLocationListener)
        }
    }

    /**
     * Function to fetch the location
     */
    private fun getLocation() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            try {
                if (hasGps == true) {
                    locationManager?.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener,
                    )
                } else {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            } catch (e: SecurityException) {
                Log.e("LocationHelper", "SecurityException: ${e.message}")
            }
        }
    }

    private val gpsLocationListener: LocationListener = LocationListener { location ->
        locationCallback(location) // Callback to handle location updates
    }
}
