package com.pete.data.di.repository

import com.pete.domain.di.model.AddressDomainModel
import com.pete.domain.di.model.OrdersListModel
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.repository.OrderRepository

class OrderRepositoryImpl (private val networkService: NetworkService):OrderRepository{
    override suspend fun placeOrder(addressDomainModel: AddressDomainModel,userId: Long):ResultWrapper<Long>{
        return networkService.placeOrder(addressDomainModel, userId)
    }

    override suspend fun getOrdersList(userId: Long): ResultWrapper<OrdersListModel>{
        return  networkService.getOrdersList(userId)
    }

}