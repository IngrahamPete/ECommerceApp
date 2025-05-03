package com.pete.domain.di.usecase

import com.pete.domain.di.repository.CartRepository

class GetCartUseCase(val  cartRepository: CartRepository) {
    suspend fun execute(userId: Long)=cartRepository.getCart(userId)
}