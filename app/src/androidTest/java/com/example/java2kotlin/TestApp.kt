package com.example.java2kotlin

import android.app.Application
import org.koin.android.ext.android.startKoin

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf())
    }
}