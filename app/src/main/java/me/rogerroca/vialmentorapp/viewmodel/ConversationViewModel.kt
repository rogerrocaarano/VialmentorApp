package me.rogerroca.vialmentorapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.local.room.ConversationsRepositoryImpl
import me.rogerroca.vialmentorapp.data.local.room.MessagesRepositoryImpl
import me.rogerroca.vialmentorapp.data.remote.api.ApiClient
import me.rogerroca.vialmentorapp.model.entity.Conversation
import me.rogerroca.vialmentorapp.model.entity.Identifier
import me.rogerroca.vialmentorapp.model.entity.Message
import me.rogerroca.vialmentorapp.model.entity.MessageState
import me.rogerroca.vialmentorapp.model.entity.MessageType
import me.rogerroca.vialmentorapp.model.repository.ConversationsRepository
import me.rogerroca.vialmentorapp.model.repository.MessagesRepository
import me.rogerroca.vialmentorapp.util.firebase.AuthManager
import java.time.Instant

class ConversationViewModel(
    private val messagesRepo: MessagesRepository,
    private val conversationsRepo: ConversationsRepository,
    private val api: ApiClient,
    private val authManager: AuthManager,
    private val conversationId: Int
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation


    init {
        updateConversation()
        updateMessages()
    }

    fun updateConversation() {
        viewModelScope.launch {
            _conversation.value = conversationsRepo.getConversation(conversationId)
        }
    }

    fun updateMessages() {
        viewModelScope.launch {
            val fetchedMessages = messagesRepo.getMessages(conversationId)
            _messages.value = fetchedMessages
        }
    }

    fun addMessage(userInput: String) {
        val message = Message(
            text = userInput,
            sendAt = Instant.now(),
            type = MessageType.USER,
            state = MessageState.SENDING
        )
        viewModelScope.launch {
            val messageId = messagesRepo.addMessage(message, conversationId)
            updateMessages()
            while (conversation.value?.cloudId?.isBlank()!!) {
                Log.d(TAG, "Waiting for cloud ID to be set")
                delay(1000)
                updateConversation()
            }
            val cloudId = sendMessage(message)
            val success = cloudId?.let {
                messagesRepo.setCloudId(messageId, it)
            }
            if (success == true) {
                Log.d(TAG, "Message sent with cloud ID: $cloudId")
                updateMessages()
            } else {
                Log.e(TAG, "Failed to set cloud ID for message")
            }
        }
    }

    private suspend fun sendMessage(message: Message): String? {
        val jwtToken = authManager.getJwtToken()
        val conversationId = conversation.value?.cloudId
        requireNotNull(conversationId)
        return if (jwtToken != null) {
            api.addMessageToConversation(jwtToken, conversationId, message.text)
        } else {
            null
        }
    }
}