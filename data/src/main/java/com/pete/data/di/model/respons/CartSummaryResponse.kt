package com.pete.data.di.model.respons

import kotlinx.serialization.Serializable

@Serializable
data class CartSummaryResponse(
    val `data`: Summary,
    val msg: String
) {
    fun toCartSummary() = com.pete.domain.di.model.CartSummary(
        data = `data`.toSummaryData(),
        msg = msg
    )
}