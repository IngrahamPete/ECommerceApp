package com.pete.domain.di

import com.pete.domain.di.usecase.AddToCartUseCase
import com.pete.domain.di.usecase.CartSummaryUseCase
import com.pete.domain.di.usecase.CategoriesUseCasse
import com.pete.domain.di.usecase.GetCartUseCase
import com.pete.domain.di.usecase.GetProductUseCase
import com.pete.domain.di.usecase.UpdateQuantityUSeCase
import org.koin.dsl.module

val  UseCaseModule = module{
    factory {GetProductUseCase(get())}
    factory {CategoriesUseCasse(get())}
    factory {AddToCartUseCase(get())}
    factory {GetCartUseCase(get())}
    factory {UpdateQuantityUSeCase(get())}
    factory {CartSummaryUseCase(get()) }

 }


