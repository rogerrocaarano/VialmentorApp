package me.rogerroca.vialmentorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import me.rogerroca.vialmentorapp.data.remote.firebase.AuthManager
import me.rogerroca.vialmentorapp.ui.navigation.AppNavHost
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.util.PermissionManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val authManager by inject<AuthManager>()
    private val permissionManager by inject<PermissionManager>()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionManager.onPermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initAuth()
        initPermissions()

        setContent {
            val navController = rememberNavController()
            VialmentorAppTheme {
                AppNavHost(navController = navController)
            }
        }
    }

    private fun initAuth() {
        authManager.signInAnonymously { success, uid ->
            if (success) {
                Log.d("MainActivity", "Signed in as: $uid")
            } else {
                Log.e("MainActivity", "Authentication failed")
            }
        }
    }

    private fun initPermissions() {
        permissionManager.registerLauncher(requestPermissionLauncher)
        permissionManager.requestNotificationPermission(this)
    }
}