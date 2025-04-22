package com.pete.data.di.model.respons
import com.pete.data.di.model.DataProductModel
import com.pete.domain.di.model.ProductListModel

import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val data: List<DataProductModel>,
    val msg: String
){
    fun toProductList()= ProductListModel(
        products = `data`.map { it.toProduct() },
        msg = msg
    )
}
