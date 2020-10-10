package com.example.countrysearch.ui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

open class BaseActivity : AppCompatActivity() {

    private val GPS_SETTINGS_REQUEST_CODE: Int = 101
    lateinit var mLocationRequest: LocationRequest


    internal fun checkPermissionDetails() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    //Todo
        if (hasPermission(*permissions)) {
            onPermissionGrant()
        } else {
            requestPermissions(permissions, 100)
        }
    }

    fun hasPermission(vararg permissions: String?): Boolean {
        for (permission in permissions) {
            if (checkCallingOrSelfPermission(permission!!) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedPermission: MutableList<String> = ArrayList()
        val grantPermission: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                grantPermission.add(permission)
            } else {
                deniedPermission.add(permission)
            }
        }
        if (deniedPermission.size > 0) {
            onPermissionDenied()
        } else {
            onPermissionGrant()
        }
    }


    fun onPermissionGrant() {
        checkGpsSettting()
    }

    fun onPermissionDenied() {
        Toast.makeText(this, "Please Provide permission", Toast.LENGTH_SHORT).show()
    }

    fun buildLocationSettingsRequest(mLocationRequest: LocationRequest): LocationSettingsRequest? {
        mLocationRequest?.let {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(it)
            return builder.build()
        }
    }

    fun checkGpsSettting() {
        var mLocationSettingsRequest =
            buildLocationSettingsRequest(createLocationRequest())
        var mSettingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener {
                onActivityResult(0, -1, null)
            }.addOnFailureListener {

                if (it is ApiException) {
                    when (it.statusCode) {

                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            var rae = it as ResolvableApiException
                            rae.startResolutionForResult(
                                this,
                                GPS_SETTINGS_REQUEST_CODE
                            )

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            }

    }

    fun createLocationRequest(): LocationRequest {
        mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        return mLocationRequest
    }


}