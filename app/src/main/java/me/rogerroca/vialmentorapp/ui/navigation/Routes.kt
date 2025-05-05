package me.rogerroca.vialmentorapp.ui.navigation

object Routes {
    const val CONVERSATIONS_LIST = "conversationsList"
    const val CONVERSATION = "conversation"
    fun goToConversation(id: Int) = "$CONVERSATION/$id"
}
