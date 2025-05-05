package me.rogerroca.vialmentorapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.rogerroca.vialmentorapp.ui.screen.ConversationScreen
import me.rogerroca.vialmentorapp.ui.screen.ConversationsListScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CONVERSATIONS_LIST
    ) {
        composable(Routes.CONVERSATIONS_LIST) {
            ConversationsListScreen(
                viewModel = koinViewModel(),
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable("${Routes.CONVERSATION}/{conversationId}") { backStackEntry ->
            val conversationId =
                backStackEntry.arguments?.getString("conversationId")?.toInt()
            requireNotNull(conversationId)
            ConversationScreen(
                viewModel = koinViewModel { parametersOf(conversationId) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
