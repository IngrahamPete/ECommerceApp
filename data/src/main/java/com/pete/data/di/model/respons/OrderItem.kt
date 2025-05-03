package com.pete.data.di.model.respons

import com.pete.domain.di.model.OrderProductItem
import kotlinx.serialization.Serializable


@Serializable
data class OrderItem(
    val id: Int,
    val orderId:Int,
    val price:Double,
    val productId:Int,
    val quantity:Int,
    val productName:String,
    val userId:Int

)
{
    fun toDomainResponse(): OrderProductItem {
        return OrderProductItem(
            id = id,
            orderId = orderId,
            price = price,
            productId = productId,
            productName = productName,
            quantity = quantity,
            userId = userId
        )
    }
}
