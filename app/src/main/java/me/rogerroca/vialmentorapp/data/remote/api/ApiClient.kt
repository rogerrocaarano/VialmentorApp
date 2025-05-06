package me.rogerroca.vialmentorapp.data.remote.api

import android.content.ContentValues.TAG
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.rogerroca.vialmentorapp.data.remote.api.dto.AddMessageToConversationResponse
import me.rogerroca.vialmentorapp.data.remote.api.dto.SendConversationResponse


class ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url { API_BASE_URL }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 120_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 120_000
        }
    }

    suspend fun registerFirebaseUserId(jwtToken: String): String {
        val url = "$API_BASE_URL$API_PATH_REGISTER_FIREBASE_USER"
        Log.d(TAG, "Requesting URL: $url")
        val response = client.get(url) {
            header("Authorization", "Bearer $jwtToken")
        }
        return response.body()
    }

    suspend fun createConversation(jwtToken: String, conversationHeader: String): String? {
        val url = "$API_BASE_URL$API_PATH_CREATE_CONVERSATION"
        Log.d(TAG, "Requesting URL: $url")
        val response = client.post(url) {
            header("Authorization", "Bearer $jwtToken")
            contentType(ContentType.Application.Json)
            setBody(mapOf("conversationHeader" to conversationHeader))
        }
        val responseDto = response.body<SendConversationResponse>()
        return responseDto.conversationId
    }

    suspend fun addMessageToConversation(jwtToken: String, conversationId: String, content: String): String {
        val url = "$API_BASE_URL$API_PATH_ADD_MESSAGE".replace("{conversationId}", conversationId)
        Log.d(TAG, "Requesting URL: $url")
        val response = client.post(url) {
            header("Authorization", "Bearer $jwtToken")
            contentType(ContentType.Application.Json)
            setBody(mapOf("content" to content))
        }
        val responseDto = response.body<AddMessageToConversationResponse>()
        return responseDto.messageId
    }

    companion object {
        private const val API_BASE_URL = "https://vialmentor-api.rogerroca.me"
        private const val API_PATH_SEND_MESSAGE = "/ChatAgent/send-message"
        private const val API_PATH_REGISTER_FIREBASE_USER = "/UserRegistration/register-firebase-user"
        private const val API_PATH_CREATE_CONVERSATION = "/Conversations/create"
        private const val API_PATH_ADD_MESSAGE = "/Conversations/{conversationId}/add-message"
    }
}

