package com.pete.domain.di.usecase

import com.pete.domain.di.model.request.AddCartRequestModel
import com.pete.domain.di.repository.CartRepository

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(request: AddCartRequestModel,userId: Long)=cartRepository.addProductToCart(request, userId)
}