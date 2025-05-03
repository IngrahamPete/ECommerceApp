package com.pete.domain.di.usecase

import com.pete.domain.di.model.AddressDomainModel
import com.pete.domain.di.repository.OrderRepository

class PlaceOrderUseCase (val orderRepository: OrderRepository){
    suspend fun execute(addressDomainModel: AddressDomainModel,userId: Long)=orderRepository.placeOrder(addressDomainModel,userId)

}