package com.example.notweshare

import android.app.Application
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            viewModel { UserViewModel() }
            viewModel { GroupViewModel() }
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules ( appModule )
        }
    }
}