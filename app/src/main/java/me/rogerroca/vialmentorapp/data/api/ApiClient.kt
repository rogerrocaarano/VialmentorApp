package me.rogerroca.vialmentorapp.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.rogerroca.vialmentorapp.data.api.dto.AgentAskQuestionRequest
import me.rogerroca.vialmentorapp.data.api.dto.AgentAskQuestionResponse


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

    companion object {
        private const val API_BASE_URL = "https://api.vialmentor.rogerroca.me"
        private const val API_PATH_SEND_MESSAGE = "/ChatAgent/send-message"
    }
}

