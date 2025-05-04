package me.rogerroca.vialmentorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.local.room.ConversationsRepositoryImpl
import me.rogerroca.vialmentorapp.model.entity.Conversation
import java.time.Instant

class ConversationsListViewModel(private val repository: ConversationsRepositoryImpl) :
    ViewModel() {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations

    private val _createdConversation = MutableStateFlow<Int?>(null)
    val createdConversation: StateFlow<Int?> = _createdConversation

    init {
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            _conversations.value = repository.getConversations()
        }
    }

    fun newConversation(heading: String) {
        val conversation = Conversation(
            heading = heading,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        viewModelScope.launch {
            val id = repository.addConversation(conversation)
            _createdConversation.value = id
        }
    }
}