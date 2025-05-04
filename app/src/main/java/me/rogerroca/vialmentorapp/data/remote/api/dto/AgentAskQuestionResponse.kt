package me.rogerroca.vialmentorapp.data.remote.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class AgentAskQuestionResponse (
    val conversationId: String,
    val messageId: String
)