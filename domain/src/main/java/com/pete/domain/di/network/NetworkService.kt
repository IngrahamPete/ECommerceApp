package com.pete.domain.di.network

import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.model.ProductListModel
import com.pete.domain.di.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories(): ResultWrapper<CategoriesListModel>

    suspend fun addProductToCart(
        request:AddCartRequestModel
    ):ResultWrapper<CartModel>

    suspend fun getCart():ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel):ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId:Int,userId:Int):ResultWrapper<CartModel>
    suspend fun getCartSummary(userId:Int):ResultWrapper<CartSummary>
}
sealed class ResultWrapper<out T>
{
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val exception: Exception):ResultWrapper<Nothing>()
}
