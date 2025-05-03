package com.pete.domain.di.repository

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.request.AddCartRequestModel
import com.pete.domain.di.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(
        request: AddCartRequestModel,userId: Long
    ): ResultWrapper<CartModel>

    suspend fun getCart(userId: Long): ResultWrapper<CartModel>
    abstract suspend fun updateQuantity(cartItemModel: CartItemModel,userId:Long):ResultWrapper<CartModel>
    abstract suspend fun deleteItem(cartItemId:Int,userId:Long):ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Long):ResultWrapper<CartSummary>

}