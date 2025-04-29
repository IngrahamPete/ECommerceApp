package com.pete.data.di.model.respons

import com.pete.domain.di.model.CartModel
import kotlinx.serialization.Serializable


@Serializable
data class CartResponse(
    val data:List<CartItem>,
    val msg:String
) {
    fun toCartModel():CartModel
    {
        return CartModel(
            data = data.map { it.toCartItemModel()},
            msg = msg
        )

    }

}
