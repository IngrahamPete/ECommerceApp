package com.pete.ecommerceapp

import android.app.Application
import com.pete.data.di.dataModule
import com.pete.domain.di.domainModule
import com.pete.ecommerceapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class EcommerceApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EcommerceApp)
            appModule
            dataModule
            domainModule
        }

    }
}