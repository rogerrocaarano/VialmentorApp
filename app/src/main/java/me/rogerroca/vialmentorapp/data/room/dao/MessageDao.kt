package me.rogerroca.vialmentorapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.rogerroca.vialmentorapp.data.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessages(conversationId: Int): List<MessageEntity>
}