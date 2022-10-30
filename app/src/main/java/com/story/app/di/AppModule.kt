package com.story.app.di

import com.story.app.ui.add.AddViewModel
import com.story.app.ui.home.HomeViewModel
import com.story.app.ui.login.LoginViewModel
import com.story.app.ui.maps.MapsViewModel
import com.story.app.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AddViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}