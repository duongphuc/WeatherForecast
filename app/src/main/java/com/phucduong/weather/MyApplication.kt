package com.phucduong.weather

import android.app.Application
import com.phucduong.weather.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            androidLogger()
            modules(listOf(appModule, sharedModule, retrofitModule, dbModule, dataSourceModule))
        }
    }
}