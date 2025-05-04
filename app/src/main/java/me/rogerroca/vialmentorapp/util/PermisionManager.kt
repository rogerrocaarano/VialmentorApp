package me.rogerroca.vialmentorapp.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionManager {

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    fun registerLauncher(launcher: ActivityResultLauncher<String>) {
        requestPermissionLauncher = launcher
    }

    fun requestNotificationPermission(activityContext: android.content.Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                activityContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                Log.d("PermissionManager", "Permission already granted")
                showNotification()
            }
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            Log.d("PermissionManager", "Permission granted")
            showNotification()
        } else {
            Log.d("PermissionManager", "Permission denied")
        }
    }

    private fun showNotification() {
        Log.d("PermissionManager", "Show notification")
    }
}