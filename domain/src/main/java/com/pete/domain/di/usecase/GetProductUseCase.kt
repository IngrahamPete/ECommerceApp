package com.pete.domain.di.usecase

import com.pete.domain.di.repository.ProductRepsoitory

class GetProductUseCase(private val repository:ProductRepsoitory) {
    suspend  fun execute()=repository.getProducts()


}