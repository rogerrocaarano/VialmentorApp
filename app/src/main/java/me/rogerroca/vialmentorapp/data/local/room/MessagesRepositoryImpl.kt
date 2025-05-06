package me.rogerroca.vialmentorapp.data.local.room

import me.rogerroca.vialmentorapp.data.local.room.dao.MessageDao
import me.rogerroca.vialmentorapp.data.local.room.entity.MessageEntity
import me.rogerroca.vialmentorapp.model.entity.Message
import me.rogerroca.vialmentorapp.model.entity.MessageState
import me.rogerroca.vialmentorapp.model.entity.MessageType
import me.rogerroca.vialmentorapp.model.repository.MessagesRepository

class MessagesRepositoryImpl(private val messageDao: MessageDao) : MessagesRepository {

    override suspend fun getMessages(id: Int): List<Message> {
        try {
            val entity = messageDao.getMessages(id)
            return entity.map { messageEntity ->
                Message(
                    id = messageEntity.id,
                    cloudId = messageEntity.cloudId,
                    text = messageEntity.text,
                    sendAt = messageEntity.sendAt,
                    type = MessageType.valueOf(messageEntity.type),
                    state = MessageState.valueOf(messageEntity.state)
                )
            }.sortedBy { it.sendAt }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun getMessagesIdCloudByConversationId(conversationId: Int): List<String> {
        try {
            return messageDao.getMessagesIdCloudByConversationId(conversationId)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun addMessage(message: Message, conversationId: Int): Int {
        try {
            val entity = MessageEntity(
                text = message.text,
                sendAt = message.sendAt,
                conversationId = conversationId,
                state = message.state.toString(),
                type = message.type.toString()
            )
            return messageDao.insertMessage(entity).toInt()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override suspend fun setCloudId(id: Int, cloudId: String): Boolean {
        try {
            val storedEntity = messageDao.getMessage(id)
            if (storedEntity != null) {
                val updatedEntity = storedEntity.copy(
                    cloudId = cloudId,
                    state = MessageState.SENT.toString()
                )
                messageDao.updateMessage(updatedEntity)
                return true
            }
            return false

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}