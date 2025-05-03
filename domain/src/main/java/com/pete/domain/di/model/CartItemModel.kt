package com.pete.domain.di.model

data class CartItemModel (
    val id: Int,
    val productId:Int,
    val price:Double,
    val imageUrl:String?=null,
    val quantity:Int,
    val productName:String
//items in cart model

)