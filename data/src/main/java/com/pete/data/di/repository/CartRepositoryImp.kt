package com.pete.data.di.repository

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.request.AddCartRequestModel
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.repository.CartRepository

class CartRepositoryImp(val networkService: NetworkService):CartRepository {
    override suspend fun addProductToCart(request: AddCartRequestModel,userId: Long):ResultWrapper<CartModel>
    {
        return networkService.addProductToCart(request,userId)
    }

    override suspend fun getCart(userId: Long): ResultWrapper<CartModel> {
        return networkService.getCart(userId)
    }

    override suspend fun updateQuantity(cartItemModel: CartItemModel,userId: Long): ResultWrapper<CartModel> {
        return networkService.updateQuantity(cartItemModel,userId)
    }

    override suspend fun deleteItem(cartItemId: Int, userId: Long): ResultWrapper<CartModel> {
    return networkService.deleteItem(cartItemId, userId)
    }

    override suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary> {
        return networkService.getCartSummary(userId)
    }

}
