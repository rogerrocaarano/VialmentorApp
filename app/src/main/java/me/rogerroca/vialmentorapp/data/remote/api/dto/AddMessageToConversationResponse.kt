package me.rogerroca.vialmentorapp.data.remote.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddMessageToConversationResponse(
    val messageId: String
)
