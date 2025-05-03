package com.pete.domain.di.usecase

import com.pete.domain.di.repository.CartRepository

class CartSummaryUseCase (private val repository: CartRepository){
    suspend fun execute(userId: Long)=repository.getCartSummary(userId)

}