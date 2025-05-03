package com.pete.domain.di.network

import com.pete.domain.di.model.AddressDomainModel
import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.model.OrdersListModel
import com.pete.domain.di.model.ProductListModel
import com.pete.domain.di.model.UserDomainModel
import com.pete.domain.di.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories(): ResultWrapper<CategoriesListModel>

    suspend fun addProductToCart(
        request:AddCartRequestModel,userId: Long
    ):ResultWrapper<CartModel>

    suspend fun getCart(userId: Long):ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel,userId: Long):ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId:Int, userId: Long):ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Long):ResultWrapper<CartSummary>
    suspend fun placeOrder(address:AddressDomainModel, userId: Long):ResultWrapper<Long>
    suspend fun getOrdersList(userId: Long):ResultWrapper<OrdersListModel>
    suspend fun login(email:String,password:String):ResultWrapper<UserDomainModel>
    suspend fun register(email:String,password:String,name:String):ResultWrapper<UserDomainModel>
}
sealed class ResultWrapper<out T>
{
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val exception: Exception):ResultWrapper<Nothing>()
}
