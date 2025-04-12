package com.pete.domain.di

import org.koin.dsl.module


val domainModule= module {
    includes(UseCaseModule)

}
