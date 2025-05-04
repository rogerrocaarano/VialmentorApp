package me.rogerroca.vialmentorapp.model.repository

import me.rogerroca.vialmentorapp.model.entity.Identifier
import me.rogerroca.vialmentorapp.model.entity.Message

interface IMessageRepository {
    suspend fun getMessages(conversation: Identifier): List<Message>
    suspend fun addMessage(message: Message, conversation: Identifier)
}