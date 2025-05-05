package me.rogerroca.vialmentorapp.data.remote.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.rogerroca.vialmentorapp.data.remote.api.dto.AgentAskQuestionRequest
import me.rogerroca.vialmentorapp.data.remote.api.dto.AgentAskQuestionResponse


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

    suspend fun postSendChatMessage(request: AgentAskQuestionRequest): AgentAskQuestionResponse {
        val response = client.post(API_PATH_SEND_MESSAGE) {
            contentType(ContentType.Application.Json)
            if (request.conversationId != null) {
                parameter("conversationId", request.conversationId)
            }
            parameter("message", request.message)
        }
        return response.body()
    }

    suspend fun registerFirebaseUserId(jwtToken: String): String {
        val url = "$API_BASE_URL$API_PATH_REGISTER_FIREBASE_USER"
        Log.d("ApiClient", "Requesting URL: $url")
        val response = client.get(url) {
            header("Authorization", "Bearer $jwtToken")
        }
        return response.body()
    }

    companion object {
        private const val API_BASE_URL = "https://vialmentor-api.rogerroca.me"
        private const val API_PATH_SEND_MESSAGE = "/ChatAgent/send-message"
        private const val API_PATH_REGISTER_FIREBASE_USER = "/UserRegistration/register-firebase-user"
    }
}

