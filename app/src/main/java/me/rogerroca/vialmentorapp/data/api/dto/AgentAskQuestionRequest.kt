package me.rogerroca.vialmentorapp.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class AgentAskQuestionRequest(
    val conversationId: String?,
    val message: String
)