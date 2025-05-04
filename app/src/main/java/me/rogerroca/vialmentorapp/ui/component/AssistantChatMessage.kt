package me.rogerroca.vialmentorapp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown

@Composable
fun AssistantChatMessage(text: String) {
    Markdown(
        text,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview
@Composable
fun AssistantChatMessagePreview() {
    AssistantChatMessage(
        text = """
                    # Hello, this is a message from the assistant!
                    
                    - This is a bullet point
                    - This is another bullet point
                    
                    **This text is bold**
                    
                    [Click here](https://www.example.com) to visit our website.
                """.trimIndent()
    )
}