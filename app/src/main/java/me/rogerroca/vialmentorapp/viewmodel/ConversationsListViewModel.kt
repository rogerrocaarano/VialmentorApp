package me.rogerroca.vialmentorapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.rogerroca.vialmentorapp.data.remote.api.ApiClient
import me.rogerroca.vialmentorapp.model.entity.Conversation
import me.rogerroca.vialmentorapp.model.repository.ConversationsRepository
import me.rogerroca.vialmentorapp.util.firebase.AuthManager
import java.time.Instant

class ConversationsListViewModel(
    private val repository: ConversationsRepository,
    private val api: ApiClient,
    private val authManager: AuthManager
) : ViewModel() {
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
            val cloudId = sendConversation(conversation)
            val success = cloudId?.let {
                repository.setConversationCloudId(id, it)
            }
            if (success == true) {
                Log.d(TAG, "Conversation created with cloud ID: $cloudId")
            } else {
                Log.e(TAG, "Failed to set cloud ID for conversation")
            }
            loadConversations()
        }
    }

    private suspend fun sendConversation(conversation: Conversation): String? {
        val jwtToken = authManager.getJwtToken()
        return if (jwtToken != null) {
            api.createConversation(jwtToken, conversation.heading)
        } else {
            Log.e(TAG, "Failed to get JWT token")
            null
        }
    }

    fun clearCreatedConversationId() {
        _createdConversation.value = null
    }
}