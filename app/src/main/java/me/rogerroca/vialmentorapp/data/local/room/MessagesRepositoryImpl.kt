package me.rogerroca.vialmentorapp.data.local.room

import me.rogerroca.vialmentorapp.data.local.room.dao.MessageDao
import me.rogerroca.vialmentorapp.data.local.room.entity.MessageEntity
import me.rogerroca.vialmentorapp.model.entity.Identifier
import me.rogerroca.vialmentorapp.model.entity.Message
import me.rogerroca.vialmentorapp.model.entity.MessageState
import me.rogerroca.vialmentorapp.model.entity.MessageType
import me.rogerroca.vialmentorapp.model.repository.MessagesRepository

class MessagesRepositoryImpl(private val messageDao: MessageDao) : MessagesRepository {

    override suspend fun getMessages(conversation: Identifier): List<Message> {
        require(conversation is Identifier.IntId)
        try {
            val entity = messageDao.getMessages(conversation.id)
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

    override suspend fun addMessage(message: Message, conversation: Identifier) {
        require(conversation is Identifier.IntId)
        try {
            val entity = MessageEntity(
                text = message.text,
                sendAt = message.sendAt,
                type = message.type.toString(),
                conversationId = conversation.id,
                state = message.state.toString()
            )
            messageDao.insertMessage(entity)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}