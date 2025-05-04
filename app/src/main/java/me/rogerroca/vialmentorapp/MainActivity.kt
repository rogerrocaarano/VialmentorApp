package me.rogerroca.vialmentorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.rogerroca.vialmentorapp.data.remote.firebase.AuthManager
import me.rogerroca.vialmentorapp.ui.screen.ConversationScreen
import me.rogerroca.vialmentorapp.ui.screen.ConversationsListScreen
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.util.PermissionManager
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val authManager by inject<AuthManager>()
    private val permissionManager by inject<PermissionManager>()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            permissionManager.onPermissionResult(isGranted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initAuth()
        initPermissions()
        initUI()
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

    private fun initUI() {
        setContent {
            val navController = rememberNavController()

            VialmentorAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "conversationsList"
                ) {
                    composable("conversationsList") {
                        ConversationsListScreen(
                            viewModel = koinViewModel(),
                            navController = navController,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable("conversation/{conversationId}") { backStackEntry ->
                        val conversationId =
                            backStackEntry.arguments?.getString("conversationId")?.toInt()
                        requireNotNull(conversationId) { "conversationId should not be null" }
                        ConversationScreen(
                            viewModel = koinViewModel { parametersOf(conversationId) }, // Pasamos directamente el ViewModel
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

}