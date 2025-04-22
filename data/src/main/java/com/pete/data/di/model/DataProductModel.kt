package com.pete.data.di.model

import kotlinx.serialization.Serializable


@Serializable
class DataProductModel(
    val categoryId:Int,
    val description:String,
    val id:Int,
    val image:String,
    val price:Double,
    val title:String
)
{
    fun toProduct()=com.pete.domain.di.model.Product(
        categoryId=categoryId,
        description=description,
        id=id,
        image=image,
        title = title,
        price = price
    )
}