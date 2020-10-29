package com.example.java2kotlin

import android.app.Application
import com.example.java2kotlin.di.appModule
import com.example.java2kotlin.di.mainModule
import com.example.java2kotlin.di.noteModule
import com.example.java2kotlin.di.splashModule
import org.koin.android.ext.android.startKoin

class App: Application() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}