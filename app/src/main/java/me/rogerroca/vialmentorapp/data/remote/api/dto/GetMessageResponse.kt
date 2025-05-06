package me.rogerroca.vialmentorapp.data.remote.api.dto

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class GetMessageResponse(
    val id: String,
    val conversationId: String,
    val text: String,
    val role: String,
    val createdAt: Date
)
