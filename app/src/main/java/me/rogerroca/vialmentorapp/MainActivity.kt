package me.rogerroca.vialmentorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.rogerroca.vialmentorapp.data.local.room.AppDb
import me.rogerroca.vialmentorapp.data.local.room.LocalConversationRepository
import me.rogerroca.vialmentorapp.data.local.room.LocalMessageRepository
import me.rogerroca.vialmentorapp.ui.screen.ConversationScreen
import me.rogerroca.vialmentorapp.ui.screen.ConversationsListScreen
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database = AppDb.getDatabase(this)
            val messageDao = database.messageDao()
            val conversationDao = database.conversationDao()
            val localMessageRepository = LocalMessageRepository(messageDao)
            val localConversationRepository = LocalConversationRepository(conversationDao)
            val navController = rememberNavController()

            VialmentorAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "conversationsList"
                ) {

                    composable("conversationsList") {
                        ConversationsListScreen(
                            ConversationsListViewModel(localConversationRepository),
                            navController = navController,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable("conversation/{conversationId}") { backStackEntry ->
                        val conversationId = backStackEntry.arguments?.getString("conversationId")?.toInt()
                        require(conversationId is Int)
                        ConversationScreen(
                            ConversationViewModel(
                                localMessageRepository,
                                localConversationRepository,
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
