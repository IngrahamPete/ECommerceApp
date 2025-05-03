package com.pete.ecommerceapp.navigation

import com.pete.ecommerceapp.model.UiProductModel
import kotlinx.serialization.Serializable


//Create routes to pages in the app
@Serializable
object HomeScreen

@Serializable
object RegisterScreen

@Serializable
object LoginScreen

@Serializable
object ProfileScreen

@Serializable
object OrdersScreen

@Serializable
object CartScreen

@Serializable
object CartSummaryScreen

@Serializable
data class ProductDetails(val product: UiProductModel)

@Serializable
data class UserAddressRoute(val userAddressWrapper: UserAddressRouteWrapper)
