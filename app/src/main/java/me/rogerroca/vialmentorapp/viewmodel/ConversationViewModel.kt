package me.rogerroca.vialmentorapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.remote.api.ApiClient
import me.rogerroca.vialmentorapp.model.entity.*
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

    companion object {
        private const val TAG = "ConversationViewModel"
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> get() = _conversation

    init {
        refreshConversation()
        refreshMessages()
    }

    fun refreshMessages() {
        viewModelScope.launch {
            _messages.value = messagesRepo.getMessages(conversationId)
        }
    }

    private fun refreshConversation() {
        viewModelScope.launch {
            _conversation.value = conversationsRepo.getConversation(conversationId)
        }
    }

    fun addMessage(userInput: String) {
        val newMessage = Message(
            text = userInput,
            sendAt = Instant.now(),
            type = MessageType.USER,
            state = MessageState.SENDING
        )

        viewModelScope.launch {
            val localId = messagesRepo.addMessage(newMessage, conversationId)
            refreshMessages()

            waitForCloudId()

            val cloudId = sendMessageToCloud(newMessage)
            if (cloudId != null && messagesRepo.setCloudId(localId, cloudId)) {
                Log.d(TAG, "Message sent with cloud ID: $cloudId")
            } else {
                Log.e(TAG, "Failed to send message or set cloud ID")
            }

            refreshMessages()
        }
    }

    private suspend fun waitForCloudId() {
        while (conversation.value?.cloudId.isNullOrBlank()) {
            Log.d(TAG, "Waiting for cloud ID...")
            delay(1000)
            refreshConversation()
        }
    }

    private suspend fun sendMessageToCloud(message: Message): String? {
        val jwtToken = authManager.getJwtToken()
        val cloudConversationId = conversation.value?.cloudId ?: return null

        return jwtToken?.let {
            api.addMessageToConversation(it, cloudConversationId, message.text)
        }
    }

    private suspend fun getMessagesIdsFromCloud(): List<String> {
        waitForCloudId()
        val jwtToken = authManager.getJwtToken() ?: return emptyList()
        val conversationCloudId = conversation.value?.cloudId ?: return emptyList()
        val cloudMessagesIds = api.getMessages(jwtToken, conversationCloudId)
        return cloudMessagesIds
    }

    fun pullMessages() {
        viewModelScope.launch {
            val cloudMessagesIds = getMessagesIdsFromCloud()
            val localMessagesCloudIds = messages.value.map { it.cloudId }
            val newMessagesIds = cloudMessagesIds.filter { it !in localMessagesCloudIds }

            if (newMessagesIds.isNotEmpty()) {

            }
        }
    }

    private suspend fun getMessageFromCloud(cloudId: String): Message? {
        waitForCloudId()
        val jwtToken = authManager.getJwtToken() ?: return null
        val conversationCloudId = conversation.value?.cloudId ?: return null
        val response = api.getMessage(jwtToken, conversationCloudId, cloudId)
        if (response == null) {
            return null
        }
        val message = Message(
            cloudId = response.id,
            type = MessageType.valueOf(response.role),
            state = when (MessageType.valueOf(response.role)) {
                MessageType.USER -> MessageState.SENT
                MessageType.ASSISTANT -> MessageState.RECEIVED
            },
            sendAt = Instant.parse(response.createdAt),
            text = response.text
        )
        return message
    }
}