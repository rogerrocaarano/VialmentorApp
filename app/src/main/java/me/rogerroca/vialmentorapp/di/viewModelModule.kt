package me.rogerroca.vialmentorapp.di

import me.rogerroca.vialmentorapp.viewmodel.ConversationViewModel
import me.rogerroca.vialmentorapp.viewmodel.ConversationsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ConversationsListViewModel(get()) }
    viewModel { (conversationId: Int) -> ConversationViewModel(get(), get(), conversationId) }
}
