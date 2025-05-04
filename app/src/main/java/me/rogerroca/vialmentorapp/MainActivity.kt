package me.rogerroca.vialmentorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import me.rogerroca.vialmentorapp.data.remote.firebase.AuthManager
import me.rogerroca.vialmentorapp.model.repository.ConversationsRepository
import me.rogerroca.vialmentorapp.model.repository.MessagesRepository
import me.rogerroca.vialmentorapp.ui.screen.ConversationScreen
import me.rogerroca.vialmentorapp.ui.screen.ConversationsListScreen
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private lateinit var permissionManager: PermissionManager

    // Koin magic
    private val conversationsRepository by inject<ConversationsRepository>()
    private val messagesRepository by inject<MessagesRepository>()
    private val authManager by inject<AuthManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authManager.signInAnonymously { success, uid ->
            if (success) {
                Log.d("MainActivity", "Signed in as: $uid")
            } else {
                Log.e("MainActivity", "Authentication failed")
            }
        }

        permissionManager = PermissionManager(this)
        permissionManager.requestPermission()

        setContent {
            val navController = rememberNavController()

            VialmentorAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "conversationsList"
                ) {

                    composable("conversationsList") {
                        ConversationsListScreen(
                            ConversationsListViewModel(conversationsRepository),
                            navController = navController,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable("conversation/{conversationId}") { backStackEntry ->
                        val conversationId =
                            backStackEntry.arguments?.getString("conversationId")?.toInt()
                        require(conversationId is Int)
                        ConversationScreen(
                            ConversationViewModel(
                                messagesRepository,
                                conversationsRepository,
                                conversationId
                            ),
                            conversationId = conversationId,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}