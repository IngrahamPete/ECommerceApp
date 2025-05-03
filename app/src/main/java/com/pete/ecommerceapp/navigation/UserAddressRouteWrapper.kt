package com.pete.ecommerceapp.navigation

import android.os.Parcelable
import com.pete.ecommerceapp.model.UserAddress
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddressRouteWrapper(
    val userAddress: UserAddress?


):Parcelable
