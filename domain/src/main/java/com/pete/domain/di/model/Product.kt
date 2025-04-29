package com.pete.domain.di.model

data class Product(
    var id: Int,
    var title: String,
    var price: Double,
    var categoryId: Int,
    var description: String,
    var image: String
){
    val priceString: String
        get()="$price"
}