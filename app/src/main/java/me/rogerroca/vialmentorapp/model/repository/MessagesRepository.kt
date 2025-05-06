package me.rogerroca.vialmentorapp.model.repository

import me.rogerroca.vialmentorapp.model.entity.Message

interface MessagesRepository {
    suspend fun getMessages(id: Int): List<Message>
    suspend fun addMessage(message: Message, conversationId: Int) : Int
    suspend fun setCloudId(id: Int, cloudId: String) : Boolean
}