package com.pete.data.di.model.respons

import com.pete.domain.di.model.OrdersListModel
import kotlinx.serialization.Serializable

@Serializable
data class OrderListResponse(
    val `data`: List<OrderListData>,
    val msg: String

){
    fun toDomainResponse(): OrdersListModel {
        return OrdersListModel(
            `data` = `data`.map { it.toDomainResponse() },
            msg = msg
        )
    }
}
