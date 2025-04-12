package com.pete.data.di

import com.pete.data.di.repository.ProductRepositoryImp
import org.koin.dsl.module

val repositoryModule=module{
    single{ProductRepositoryImp(get()) }
}