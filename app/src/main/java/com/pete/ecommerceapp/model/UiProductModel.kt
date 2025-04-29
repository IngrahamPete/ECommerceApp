package com.pete.ecommerceapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class UiProductModel(
    var id: Int,
    var title: String,
    var price: Double,
    var categoryId: Int,
    var description: String,
    var image: String
) : Parcelable {
    companion object {
        fun fromProduct(product: com.pete.domain.di.model.Product) = UiProductModel(
            id=product.id,
            title=product.title,
            price=product.price,
            categoryId=product.categoryId,
            description=product.description,
            image=product.image

        )
    }
}
