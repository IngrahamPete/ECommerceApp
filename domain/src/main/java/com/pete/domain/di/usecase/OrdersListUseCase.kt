package com.pete.domain.di.usecase

import com.pete.domain.di.repository.OrderRepository

class OrdersListUseCase(private val repository: OrderRepository) {
    suspend fun execute(userId: Long)= repository.getOrdersList(userId)
}
