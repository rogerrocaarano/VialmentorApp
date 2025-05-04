package me.rogerroca.vialmentorapp.model.repository

import me.rogerroca.vialmentorapp.model.entity.Conversation

interface IConversationRepository {
    suspend fun getConversations(): List<Conversation>
    suspend fun getConversation(id: Int): Conversation?
    suspend fun addConversation(conversation: Conversation) : Int
}