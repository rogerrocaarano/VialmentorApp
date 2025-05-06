package me.rogerroca.vialmentorapp.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import me.rogerroca.vialmentorapp.R
import me.rogerroca.vialmentorapp.ui.component.BottomNavBar
import me.rogerroca.vialmentorapp.ui.component.ConversationListItem
import me.rogerroca.vialmentorapp.ui.navigation.Routes
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel

@Composable
fun ConversationsListScreen(
    viewModel: ConversationsListViewModel,
    navController: NavController,
    modifier: Modifier
) {
    val conversations by viewModel.conversations.collectAsState()
    val createdConversationId by viewModel.createdConversation.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(createdConversationId) {
        createdConversationId?.let { id ->
            navController.navigate(Routes.goToConversation(id))
            viewModel.clearCreatedConversationId()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(currentRoute ?: "") { selectedItem ->
                navController.navigate(selectedItem.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.newConversation("Test nueva conversación")
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mode_comment),
                    contentDescription = "New conversation"
                )
            }
        },
        modifier = modifier

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(conversations.size) {
                val conversation = conversations[it]
                ConversationListItem(
                    title = conversation.heading,
                    lastMessageDateTime = conversation.updatedAt.toString(),
                    onclick = {
                        navController.navigate(Routes.goToConversation(conversation.id))
                    }
                )
            }
        }
    }
}
