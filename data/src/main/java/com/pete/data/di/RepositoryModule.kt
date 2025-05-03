package com.pete.data.di

import com.pete.data.di.repository.CategoryRepositoryImp
import com.pete.data.di.repository.ProductRepositoryImp
import com.pete.domain.di.repository.CartRepository
import com.pete.domain.di.repository.CategoryRepository
import com.pete.domain.di.repository.OrderRepository
import com.pete.domain.di.repository.ProductRepository
import com.pete.domain.di.repository.UserRepository
import org.koin.dsl.module

val repositoryModule=module{
    single<ProductRepository> {ProductRepositoryImp(get()) }
    single <CategoryRepository> { CategoryRepositoryImp(get()) }
    single <CartRepository> {com.pete.data.di.repository.CartRepositoryImp(get())}
    single <OrderRepository> {com.pete.data.di.repository.OrderRepositoryImpl(get())}
    single <UserRepository> {com.pete.data.di.repository.UserRepositoryImp(get())}
}

//binds the repository to the viewmodel