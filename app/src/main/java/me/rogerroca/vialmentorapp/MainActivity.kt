package me.rogerroca.vialmentorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.remote.api.ApiClient
import me.rogerroca.vialmentorapp.util.firebase.AuthManager
import me.rogerroca.vialmentorapp.ui.navigation.AppNavHost
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.util.PermissionManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val authManager by inject<AuthManager>()
    private val permissionManager by inject<PermissionManager>()
    private val apiClient by inject<ApiClient>()
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
        authManager.signInAnonymously { isSuccess, uid ->
            if (!isSuccess) {
                Log.e("MainActivity", "Failed to sign in anonymously: $uid")
                return@signInAnonymously
            }

            Log.d("MainActivity", "Signed in anonymously with UID: $uid")
            authManager.getIdToken { tokenSuccess, idToken ->
                if (!tokenSuccess || idToken == null) {
                    Log.e("MainActivity", "Failed to obtain ID Token")
                    return@getIdToken
                }

                Log.d("MainActivity", "Obtained ID Token: $idToken")
                registerFirebaseUserId(idToken)
            }
        }
    }

    private fun registerFirebaseUserId(idToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiClient.registerFirebaseUserId(idToken)
                Log.d("MainActivity", "Firebase ID registered successfully: $response")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error registering Firebase ID: ${e.message}", e)
            }
        }
    }

    private fun initPermissions() {
        permissionManager.registerLauncher(requestPermissionLauncher)
        permissionManager.requestNotificationPermission(this)
    }
}