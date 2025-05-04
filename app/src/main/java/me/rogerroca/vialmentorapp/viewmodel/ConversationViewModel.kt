package me.rogerroca.vialmentorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.local.room.LocalConversationRepository
import me.rogerroca.vialmentorapp.data.local.room.LocalMessageRepository
import me.rogerroca.vialmentorapp.model.entity.Conversation
import me.rogerroca.vialmentorapp.model.entity.Identifier
import me.rogerroca.vialmentorapp.model.entity.Message
import me.rogerroca.vialmentorapp.model.entity.MessageState
import me.rogerroca.vialmentorapp.model.entity.MessageType
import java.time.Instant

class ConversationViewModel(
    private val messagesRepo: LocalMessageRepository,
    private val conversationsRepo: LocalConversationRepository,
    private val conversationId: Int
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val conversationIdentifier = Identifier.IntId(conversationId)


    // TODO: replace with actual conversation ID
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
            val fetchedMessages = messagesRepo.getMessages(conversationIdentifier)
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
            messagesRepo.addMessage(message, conversationIdentifier)
            updateMessages()
        }
    }
}