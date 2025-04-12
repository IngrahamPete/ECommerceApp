package com.pete.data.di

import com.pete.domain.di.domainModule
import org.koin.dsl.module

val dataModule= module {
    includes(networkModule, repositoryModule)
}
