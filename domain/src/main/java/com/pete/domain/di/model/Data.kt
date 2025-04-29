package com.pete.domain.di.model

data class SummaryData(
    val discount:Double,
    val items:List<CartItemModel>,
    val shipping:Double,
    val subtotal:Double,
    val total:Double,
    val tax:Double
)
