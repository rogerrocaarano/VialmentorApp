package me.rogerroca.vialmentorapp.di

import me.rogerroca.vialmentorapp.util.firebase.AuthManager
import me.rogerroca.vialmentorapp.util.PermissionManager
import org.koin.dsl.module

val utilsModule = module {
    single { AuthManager() }
    single { PermissionManager() }
}
