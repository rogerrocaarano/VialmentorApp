package me.rogerroca.vialmentorapp.model.entity

import java.time.Instant

data class Conversation(
    val id: Int = 0,
    val cloudId: String = "",
    val heading: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val hasNewMessage: Boolean = false
) {
    init {
        require(heading.isNotBlank())
    }

    fun isSynchronized(): Boolean {
        return cloudId.isNotBlank()
    }
}
