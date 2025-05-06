package me.rogerroca.vialmentorapp.ui.navigation

import me.rogerroca.vialmentorapp.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem(Routes.CONVERSATIONS_LIST, R.drawable.ic_launcher_foreground, "Chat")
//    object Chat : BottomNavItem("chat", R.drawable.ic_chat, "Chat")
//    object Profile : BottomNavItem("profile", R.drawable.ic_profile, "Perfil")

    companion object {
        val items = listOf(
            Home,
//            Chat,
//            Profile
        )
    }
}
