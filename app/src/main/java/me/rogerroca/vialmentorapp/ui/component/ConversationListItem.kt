package me.rogerroca.vialmentorapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ConversationListItem(title: String, lastMessageDateTime: String, onclick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onclick()
            }
    ) {
        Column {
            Text(title)
            Text(lastMessageDateTime)
        }

        // TODO: reemplazar por un icono
        Spacer(modifier = Modifier.weight(1f))
        Text(">")
    }
}

@Preview
@Composable
fun ConversationItemPreview() {
    ConversationListItem(
        title = "Conversación 1",
        lastMessageDateTime = "DD/MM/YYYY HH:mm",
        { println("Clicked") })
}