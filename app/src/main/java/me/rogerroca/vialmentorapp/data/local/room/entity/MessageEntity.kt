package me.rogerroca.vialmentorapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cloudId: String = "",
    val text: String,
    val conversationId: Int,
    val sendAt: Instant,
    val type: String,
    val state: String
)
