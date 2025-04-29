package com.pete.domain.di.repository

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.request.AddCartRequestModel
import com.pete.domain.di.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(
        request: AddCartRequestModel
    ): ResultWrapper<CartModel>

    suspend fun getCart(): ResultWrapper<CartModel>
    abstract suspend fun updateQuantity(cartItemModel: CartItemModel):ResultWrapper<CartModel>
    abstract suspend fun deleteItem(cartItemId:Int,userId:Int):ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Int):ResultWrapper<CartSummary>

}