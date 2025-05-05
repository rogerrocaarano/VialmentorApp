package me.rogerroca.vialmentorapp

import android.app.Application
import me.rogerroca.vialmentorapp.di.databaseModule
import me.rogerroca.vialmentorapp.di.repositoryModule
import me.rogerroca.vialmentorapp.di.utilsModule
import me.rogerroca.vialmentorapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VialmentorApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@VialmentorApp)
            modules(
                databaseModule,
                repositoryModule,
                viewModelModule,
                utilsModule
            )
        }
    }
}