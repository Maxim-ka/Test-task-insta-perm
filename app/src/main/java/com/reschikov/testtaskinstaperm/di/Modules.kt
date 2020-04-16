package com.reschikov.testtaskinstaperm.di

import androidx.room.Room
import com.reschikov.testtaskinstaperm.data.cache.CacheProvider
import com.reschikov.testtaskinstaperm.data.cache.Storable
import com.reschikov.testtaskinstaperm.data.cache.database.AppDatabase
import com.reschikov.testtaskinstaperm.data.network.ServerRequest
import com.reschikov.testtaskinstaperm.data.repository.Mapping
import com.reschikov.testtaskinstaperm.data.repository.Repository
import com.reschikov.testtaskinstaperm.ui.Derivable
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val NAME_DATABASE = "cache.db"

val appModule = module {
    single<Derivable> {
        Repository(ServerRequest(androidContext()), CacheProvider(get()), Mapping())
    }
}

val storeModule = module {
    single<Storable> {
        Room.databaseBuilder(get(), AppDatabase::class.java, NAME_DATABASE)
            .build()
            .signalsDao()
    }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}