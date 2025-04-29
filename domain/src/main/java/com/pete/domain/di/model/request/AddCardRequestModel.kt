package com.pete.domain.di.model.request

data class AddCartRequestModel(
    val productId:Int,
    val productName:String,
    val price:Double,
    val quantity:Int,
    val userId:Int//link cart with user
)
