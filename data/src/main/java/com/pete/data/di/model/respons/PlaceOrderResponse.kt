package com.pete.data.di.model.respons

import kotlinx.serialization.Serializable


@Serializable
data class PlaceOrderResponse(
    val `data`:OrderD,
    val msg:String

)
