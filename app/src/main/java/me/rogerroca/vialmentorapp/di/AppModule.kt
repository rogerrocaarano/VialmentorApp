package me.rogerroca.vialmentorapp.di

import me.rogerroca.vialmentorapp.data.local.room.AppDb
import me.rogerroca.vialmentorapp.data.local.room.ConversationsRepositoryImpl
import me.rogerroca.vialmentorapp.data.local.room.MessagesRepositoryImpl
import me.rogerroca.vialmentorapp.data.remote.firebase.AuthManager
import me.rogerroca.vialmentorapp.model.repository.ConversationsRepository
import me.rogerroca.vialmentorapp.model.repository.MessagesRepository
import me.rogerroca.vialmentorapp.util.PermissionManager
import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDb.getDatabase(androidContext()) }
    single { get<AppDb>().conversationDao() }
    single { get<AppDb>().messageDao() }

    single<ConversationsRepository> { ConversationsRepositoryImpl(get()) }
    single<MessagesRepository> { MessagesRepositoryImpl(get()) }

    viewModel { ConversationsListViewModel(get()) }
    viewModel { (conversationId: Int) -> ConversationViewModel(get(), get(), conversationId) }

    single { AuthManager() }
    single { PermissionManager() }
}