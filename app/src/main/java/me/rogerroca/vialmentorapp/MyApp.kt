package me.rogerroca.vialmentorapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.rogerroca.vialmentorapp.data.local.room.AppDb
import me.rogerroca.vialmentorapp.data.local.room.ConversationsRepositoryImpl
import me.rogerroca.vialmentorapp.data.local.room.MessagesRepositoryImpl
import me.rogerroca.vialmentorapp.ui.screen.ConversationScreen
import me.rogerroca.vialmentorapp.ui.screen.ConversationsListScreen
import me.rogerroca.vialmentorapp.ui.theme.VialmentorAppTheme
import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel

@Composable
fun MyApp(activity: MainActivity) {
    val database = AppDb.getDatabase(activity)
    val messageDao = database.messageDao()
    val conversationDao = database.conversationDao()
    val messagesRepositoryImpl = MessagesRepositoryImpl(messageDao)
    val conversationsRepositoryImpl = ConversationsRepositoryImpl(conversationDao)
    val navController = rememberNavController()

    VialmentorAppTheme {
        NavHost(
            navController = navController,
            startDestination = "conversationsList"
        ) {

            composable("conversationsList") {
                ConversationsListScreen(
                    ConversationsListViewModel(conversationsRepositoryImpl),
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
                        messagesRepositoryImpl,
                        conversationsRepositoryImpl,
                        conversationId
                    ),
                    conversationId = conversationId,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
