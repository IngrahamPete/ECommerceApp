package com.pete.domain.di.usecase

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.repository.CartRepository

class DeleteProductUSeCase (private val cartRepository: CartRepository){
    suspend fun execute(cartItemId:Int,userId:Int)=cartRepository.deleteItem(cartItemId,userId)
}