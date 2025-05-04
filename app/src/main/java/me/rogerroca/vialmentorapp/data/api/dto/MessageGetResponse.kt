package me.rogerroca.vialmentorapp.data.api.dto

import kotlinx.serialization.Serializable


@Serializable
data class MessageGetResponse(
    val id: String,
    val conversationId: String,
    val content: String,
    val role: String,
    val createdAt: String,
)
