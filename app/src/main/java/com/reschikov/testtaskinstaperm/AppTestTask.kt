package com.reschikov.testtaskinstaperm

import android.app.Application
import com.reschikov.testtaskinstaperm.di.appModule
import com.reschikov.testtaskinstaperm.di.storeModule
import com.reschikov.testtaskinstaperm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppTestTask : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext (this@AppTestTask)
            modules(listOf(appModule, storeModule, viewModelModule))
        }
    }
}