package com.pete.data.di.model.request

import com.pete.domain.di.model.request.AddCartRequestModel
import kotlinx.serialization.Serializable


@Serializable
data class AddToCartRequest (
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int
) {
   companion object {
       fun fromCartRequestModel(addCartRequestModel: AddCartRequestModel) = AddToCartRequest(
           productId = addCartRequestModel.productId,
           productName = addCartRequestModel.productName,
           price = addCartRequestModel.price,
           quantity = addCartRequestModel.quantity
       )
   }
}