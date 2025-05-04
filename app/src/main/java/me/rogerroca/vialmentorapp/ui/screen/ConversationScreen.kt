package me.rogerroca.vialmentorapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.rogerroca.vialmentorapp.model.entity.MessageType
import me.rogerroca.vialmentorapp.ui.component.AssistantChatMessage
import me.rogerroca.vialmentorapp.ui.component.ChatInput
import me.rogerroca.vialmentorapp.ui.component.UserChatMessage
import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel

const val CHAT_INPUT_PLACEHOLDER = "Haga una consulta aquí..."

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel,
    modifier: Modifier
) {
    val messages by viewModel.messages.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    when (message.type) {
                        MessageType.USER -> UserChatMessage(message.text, message.state)
                        MessageType.ASSISTANT -> AssistantChatMessage(message.text)
                    }
                }
            }

            ChatInput(CHAT_INPUT_PLACEHOLDER) {
                if (it.isNotBlank()) {
                    viewModel.addMessage(it)
                }
            }
        }
    }
}
