package me.rogerroca.vialmentorapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.rogerroca.vialmentorapp.data.room.dao.ConversationDao
import me.rogerroca.vialmentorapp.data.room.dao.MessageDao
import me.rogerroca.vialmentorapp.data.room.entity.ConversationEntity
import me.rogerroca.vialmentorapp.data.room.entity.MessageEntity

@Database(entities = [MessageEntity::class, ConversationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null
        fun getDatabase(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "vialmentor_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}