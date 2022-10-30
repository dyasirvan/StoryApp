package com.story.app

import android.app.Application
import com.story.app.core.di.databaseModule
import com.story.app.core.di.networkModule
import com.story.app.core.di.repositoryModule
import com.story.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelModule,
                    databaseModule
                )
            )
        }
    }
}