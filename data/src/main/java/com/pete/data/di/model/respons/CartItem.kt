package com.pete.data.di.model.respons

import com.pete.domain.di.model.CartItemModel
import kotlinx.serialization.Serializable

@Serializable
class CartItem(
    val id: Int,
    val productId:Int,
    val price:Double,
    val imageUrl:String?,
    val quantity:Int,
    val productName:String
)
{
    fun toCartItemModel():CartItemModel {
        return CartItemModel(
            id =id,
            productId = productId,
            price = price,
            imageUrl = imageUrl,
            quantity = quantity,
            productName = productName
        )
    }
}