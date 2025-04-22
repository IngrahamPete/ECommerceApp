package com.pete.domain.di.usecase

import com.pete.domain.di.repository.ProductRepository

class GetProductUseCase(private val repository:ProductRepository) {
    suspend  fun execute(category: Int?)=repository.getProducts(category)
}