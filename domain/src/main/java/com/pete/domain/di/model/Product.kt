package com.pete.domain.di.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val image: String
)