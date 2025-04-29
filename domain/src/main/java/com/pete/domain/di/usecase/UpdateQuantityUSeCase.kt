package com.pete.domain.di.usecase

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.repository.CartRepository

class UpdateQuantityUSeCase (private val cartRepository: CartRepository){
    suspend fun execute(cartItemModel: CartItemModel)=cartRepository.updateQuantity(cartItemModel)
}