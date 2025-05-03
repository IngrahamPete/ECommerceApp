package com.pete.data.di.model.request

import com.pete.domain.di.model.AddressDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class AddressDataModel(

    val city: String,
    val state: String,
    val addressLine:String,
    val postalCode: String,
    val country: String,
)
{
    companion object{
        fun fromDomainAddress(userAddress:AddressDomainModel)= AddressDataModel(
            userAddress.city,
           userAddress.state,
            userAddress.addressLine,
            userAddress.postalCode,
            userAddress.country
        )
    }
}
