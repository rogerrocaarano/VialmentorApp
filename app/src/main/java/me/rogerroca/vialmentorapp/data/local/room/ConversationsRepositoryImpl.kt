package me.rogerroca.vialmentorapp.data.local.room

import me.rogerroca.vialmentorapp.data.local.room.dao.ConversationDao
import me.rogerroca.vialmentorapp.data.local.room.entity.ConversationEntity
import me.rogerroca.vialmentorapp.model.entity.Conversation
import me.rogerroca.vialmentorapp.model.repository.ConversationsRepository

class ConversationsRepositoryImpl(private val conversationDao: ConversationDao) :
    ConversationsRepository {
    override suspend fun getConversations(): List<Conversation> {
        try {
            val entity = conversationDao.getConversations()
            return entity.map { conversationEntity ->
                Conversation(
                    id = conversationEntity.id,
                    cloudId = conversationEntity.cloudId,
                    heading = conversationEntity.heading,
                    createdAt = conversationEntity.createdAt,
                    updatedAt = conversationEntity.updatedAt,
                    hasNewMessage = conversationEntity.hasNewMessage
                )
            }.sortedBy { it.updatedAt }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun getConversation(id: Int): Conversation? {
        try {
            val entity = conversationDao.getConversation(id)
            return entity?.let {
                Conversation(
                    id = it.id,
                    cloudId = it.cloudId,
                    heading = it.heading,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    hasNewMessage = it.hasNewMessage
                )
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun addConversation(conversation: Conversation): Int {
        try {
            val entity = ConversationEntity(
                cloudId = conversation.cloudId,
                heading = conversation.heading,
                createdAt = conversation.createdAt,
                updatedAt = conversation.updatedAt,
                hasNewMessage = conversation.hasNewMessage
            )
            return conversationDao.insertConversation(entity).toInt()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun setConversationCloudId(id: Int, cloudId: String): Boolean {
        try {
            val storedEntity = conversationDao.getConversation(id)
            if (storedEntity != null) {
                val updatedEntity = storedEntity.copy(cloudId = cloudId)
                conversationDao.updateConversation(updatedEntity)
                return true
            }
            return false
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun setConversationHeading(id: Int, heading: String): Boolean {
        try {
            val storedEntity = conversationDao.getConversation(id)
            if (storedEntity != null) {
                val updatedEntity = storedEntity.copy(heading = heading)
                conversationDao.updateConversation(updatedEntity)
                return true
            }
            return false
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}