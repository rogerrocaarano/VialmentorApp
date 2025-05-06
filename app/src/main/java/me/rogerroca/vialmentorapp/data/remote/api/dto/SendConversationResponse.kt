package me.rogerroca.vialmentorapp.data.remote.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendConversationResponse(
    val conversationId: String?
)