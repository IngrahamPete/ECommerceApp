package com.pete.domain.di.network

import com.pete.domain.di.model.Product

interface NetworkService {
    suspend fun getProducts(): ResultWrapper<List<Product>>
}
sealed class ResultWrapper<out T>
{
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val exception: Exception):ResultWrapper<Nothing>()
}
