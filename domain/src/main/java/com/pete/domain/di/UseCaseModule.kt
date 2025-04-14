package com.pete.domain.di

import com.pete.domain.di.usecase.GetProductUseCase
import org.koin.dsl.module

val  UseCaseModule = module{
    factory {GetProductUseCase(get())}
 }

