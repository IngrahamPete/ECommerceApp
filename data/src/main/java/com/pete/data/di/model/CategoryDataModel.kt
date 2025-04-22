package com.pete.data.di.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDataModel(
    val id: Int,
    val title: String,
    val image: String

)
{
    fun toCategory()= com.pete.domain.di.model.Category(
        id = id,
        title = title,
        image = image
    )
}
