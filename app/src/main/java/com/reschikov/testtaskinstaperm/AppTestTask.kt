package com.reschikov.testtaskinstaperm

import android.app.Application
import com.reschikov.testtaskinstaperm.di.appModule
import com.reschikov.testtaskinstaperm.di.viewModelModule
import org.koin.core.context.startKoin

class AppTestTask : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(listOf(appModule, viewModelModule)) }
    }
}