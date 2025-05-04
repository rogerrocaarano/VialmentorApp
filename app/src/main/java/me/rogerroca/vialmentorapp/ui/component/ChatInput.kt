package me.rogerroca.vialmentorapp.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ChatInput(placeholder: String, onSendEvent: (String) -> Unit ) {
    val userInput = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = userInput.value,
            onValueChange = { userInput.value = it },
            placeholder = { Text(placeholder) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onSendEvent(userInput.value)
                userInput.value = ""
            }
        ) {
            Text("Send")
        }
    }
}

@Preview
@Composable
fun ChatInputPreview() {
    ChatInput(placeholder = "Type your message here...", onSendEvent = {})
}