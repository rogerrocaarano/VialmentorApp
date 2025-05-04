package me.rogerroca.vialmentorapp.model.entity

enum class MessageState {
    SENDING, // User message is being sent to the assistant
    SENT, // User message has been sent to the assistant
    RECEIVED // Assistant is creating a response for the user message
}