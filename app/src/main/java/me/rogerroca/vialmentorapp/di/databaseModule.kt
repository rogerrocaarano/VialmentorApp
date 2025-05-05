package me.rogerroca.vialmentorapp.di

import me.rogerroca.vialmentorapp.data.local.room.AppDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { AppDb.getDatabase(androidContext()) }
    single { get<AppDb>().conversationDao() }
    single { get<AppDb>().messageDao() }
}
