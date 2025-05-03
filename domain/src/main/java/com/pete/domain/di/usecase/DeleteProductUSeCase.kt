package com.pete.domain.di.usecase

import com.pete.domain.di.repository.CartRepository

class DeleteProductUSeCase (private val cartRepository: CartRepository){
    suspend fun execute(cartItemId:Int,userId:Long)=cartRepository.deleteItem(cartItemId,userId)
}