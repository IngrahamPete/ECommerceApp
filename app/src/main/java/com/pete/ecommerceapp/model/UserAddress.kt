package com.pete.ecommerceapp.model

import android.os.Parcelable
import com.pete.domain.di.model.AddressDomainModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddress(
    val city: String,
    val state: String,
    val addressLine:String,
    val postalCode: String,
    val country: String,
): Parcelable
{
    override fun toString(): String {
        return "$addressLine, $city, $state, $postalCode, $country"
    }

    fun toAddressDataModel() = AddressDomainModel(
         city,
       state,
         addressLine,
        country,
        postalCode,
    )
}
