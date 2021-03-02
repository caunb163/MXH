package com.caunb163.mxh.ultis

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class CheckPermission {
    companion object {
        val listPermission =
            mutableListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)

        fun checkPermission(context: Context): Boolean {
            if (listPermission.isNullOrEmpty()) {
                for (permission in listPermission) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        return false
                }
            }
            return true
        }
    }

}