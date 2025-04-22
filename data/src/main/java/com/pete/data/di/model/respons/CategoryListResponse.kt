package com.pete.data.di.model.respons

import com.pete.data.di.model.CategoryDataModel
import kotlinx.serialization.Serializable

@Serializable
data class CategoryListResponse(
    val `data`: List<CategoryDataModel>,
    val msg: String,
){
    fun toCategoryList()= com.pete.domain.di.model.CategoriesListModel(
        categories = `data`.map { it.toCategory() },
        msg=msg
    )
}
