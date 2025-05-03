package com.pete.domain.di.repository

import com.pete.domain.di.model.AddressDomainModel
import com.pete.domain.di.model.OrdersListModel
import com.pete.domain.di.network.ResultWrapper

interface OrderRepository

{
    suspend fun placeOrder(addressDomainModel: AddressDomainModel,userId: Long):ResultWrapper<Long>
    suspend fun getOrdersList(userId: Long):ResultWrapper<OrdersListModel>

}