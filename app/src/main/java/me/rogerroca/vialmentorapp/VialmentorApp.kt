package me.rogerroca.vialmentorapp

import android.app.Application
import me.rogerroca.vialmentorapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VialmentorApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@VialmentorApp)
            modules(appModule)
        }
    }
}