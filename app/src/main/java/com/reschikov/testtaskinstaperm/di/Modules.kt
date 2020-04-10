package com.reschikov.testtaskinstaperm.di

import com.reschikov.testtaskinstaperm.data.network.ServerRequest
import com.reschikov.testtaskinstaperm.data.repository.Repository
import com.reschikov.testtaskinstaperm.ui.Derivable
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Derivable> { Repository(ServerRequest()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}