package me.rogerroca.vialmentorapp.model.entity

import java.time.Instant

data class Message(
    val id: Int = 0,
    val cloudId: String = "",
    val text: String,
    val sendAt: Instant,
    val type: MessageType,
    val state: MessageState
) {
    init {
        when (type) {
            MessageType.USER -> validateUserMessage()
            MessageType.ASSISTANT -> validateAssistantMessage()
        }
    }

    private fun validateUserMessage() {
        require(
            when (state) {
                MessageState.SENDING -> true
                MessageState.SENT -> true
                else -> false
            }
        )
    }

    private fun validateAssistantMessage() {
        require(
            when (state) {
                MessageState.RECEIVED -> true
                else -> false
            }
        )
    }
}
