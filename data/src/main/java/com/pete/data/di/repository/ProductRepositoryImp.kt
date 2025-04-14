package com.pete.data.di.repository

import com.pete.domain.di.model.Product
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.repository.ProductRepository

class ProductRepositoryImp(private val networkService: NetworkService): ProductRepository {
    override  suspend fun getProducts(): ResultWrapper<List<Product>> {
        return networkService.getProducts()
    }
}