package me.rogerroca.vialmentorapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.rogerroca.vialmentorapp.model.entity.MessageState

@Composable
fun UserChatMessage(text: String, state: MessageState) {
    val backgroundColor = when (state) {
        MessageState.SENDING -> MaterialTheme.colorScheme.secondaryContainer
        MessageState.SENT -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp),
            text = text
        )
    }
}

@Preview
@Composable
fun UserChatMessagePreview() {
    UserChatMessage(text = "Hello, this is a user message!", state = MessageState.SENT)
}
