package me.rogerroca.vialmentorapp.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cloudId: String = "",
    val heading: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val hasNewMessage: Boolean = false
)

