package com.pete.domain.di.repository

import com.pete.domain.di.model.ProductListModel
import com.pete.domain.di.network.ResultWrapper

interface ProductRepository {
    suspend fun getProducts(category: Int?):ResultWrapper <ProductListModel>

}