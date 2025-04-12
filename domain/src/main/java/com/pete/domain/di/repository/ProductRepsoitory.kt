package com.pete.domain.di.repository

import com.pete.domain.di.model.Product
import com.pete.domain.di.network.ResultWrapper

interface ProductRepsoitory {
    suspend fun getProducts():ResultWrapper <List<Product>>

}