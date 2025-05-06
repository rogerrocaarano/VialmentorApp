package me.rogerroca.vialmentorapp.data.remote.api

import android.content.ContentValues.TAG
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.rogerroca.vialmentorapp.data.remote.api.dto.AddMessageToConversationResponse
import me.rogerroca.vialmentorapp.data.remote.api.dto.GetMessageResponse
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
        val url = "$API_BASE_URL$REGISTER_FIREBASE_USER"
        Log.d(TAG, "Requesting URL: $url")
        val response = client.get(url) {
            header("Authorization", "Bearer $jwtToken")
        }
        return response.body()
    }

    suspend fun createConversation(jwtToken: String, conversationHeader: String): String? {
        val url = "$API_BASE_URL$CREATE_CONVERSATION"
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
        val url = "$API_BASE_URL$ADD_MESSAGE".replace("{conversationId}", conversationId)
        Log.d(TAG, "Requesting URL: $url")
        val response = client.post(url) {
            header("Authorization", "Bearer $jwtToken")
            contentType(ContentType.Application.Json)
            setBody(mapOf("content" to content))
        }
        val responseDto = response.body<AddMessageToConversationResponse>()
        return responseDto.messageId
    }

    suspend fun getMessages(jwtToken: String, conversationId: String): List<String> {
        val url = "$API_BASE_URL$GET_MESSAGES".replace("{conversationId}", conversationId)
        Log.d(TAG, "Requesting URL: $url")
        val response = client.get(url) {
            header("Authorization", "Bearer $jwtToken")
        }
        return response.body()
    }

    suspend fun getMessage(jwtToken: String, conversationId: String, messageId: String): GetMessageResponse? {
        val url = "$API_BASE_URL$GET_MESSAGE"
            .replace("{conversationId}", conversationId)
            .replace("{messageId}", messageId)
        Log.d(TAG, "Requesting URL: $url")
        return try {
            val response = client.get(url) {
                header("Authorization", "Bearer $jwtToken")
            }
            response.body()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                Log.w(TAG, "Message not found: $messageId in conversation $conversationId")
                null
            } else {
                throw e
            }
        }
    }

    companion object {
        private const val API_BASE_URL = "https://vialmentor-api.rogerroca.me"
        private const val REGISTER_FIREBASE_USER = "/UserRegistration/register-firebase-user"
        private const val CREATE_CONVERSATION = "/Conversations/create"
        private const val ADD_MESSAGE = "/Conversations/{conversationId}/add-message"
        private const val GET_MESSAGES = "/Conversations/{conversationId}/messages"
        private const val GET_MESSAGE = "/Conversations/{conversationId}/messages/{messageId}"
    }
}

