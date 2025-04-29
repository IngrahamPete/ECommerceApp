package com.pete.data.di.model.respons

import com.pete.domain.di.model.SummaryData
import kotlinx.serialization.Serializable


@Serializable
data class Summary(
    val discount:Double,
    val items:List<CartItem>,
    val shipping:Double,
    val subtotal:Double,
    val total:Double,
    val tax:Double
)
{
    fun toSummaryData()=SummaryData(
        discount=discount,
        shipping=shipping,
        subtotal=subtotal,
        total=total,
        tax=tax,
        items = items.map { it.toCartItemModel()}
    )

}
