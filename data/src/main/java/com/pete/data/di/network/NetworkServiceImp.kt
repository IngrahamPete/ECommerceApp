package com.pete.data.di.network

import com.pete.data.di.model.request.AddToCartRequest
import com.pete.data.di.model.request.AddressDataModel
import com.pete.data.di.model.request.LoginRequest
import com.pete.data.di.model.respons.CartResponse
import com.pete.data.di.model.respons.CartSummaryResponse
import com.pete.data.di.model.respons.CategoryListResponse
import com.pete.data.di.model.respons.OrderListResponse
import com.pete.data.di.model.respons.PlaceOrderResponse
import com.pete.data.di.model.respons.ProductListResponse
import com.pete.data.di.model.respons.UserAuthResponse
import com.pete.domain.di.model.AddressDomainModel
import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartModel
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.model.OrdersListModel
import com.pete.domain.di.model.ProductListModel
import com.pete.domain.di.model.UserDomainModel
import com.pete.domain.di.model.request.AddCartRequestModel
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class NetworkServiceImp(
    private val client: HttpClient,
    private val baseUrl: String = "https://ecommerce-ktor-4641e7ff1b63.herokuapp.com/v2"
) : NetworkService {

    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        val url =
            if (category != null) "$baseUrl/products/category/$category"
        else 
            "$baseUrl/products"
            
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { dataModels: ProductListResponse ->
                dataModels.toProductList()
            }
        )
    }

    override suspend fun getCategories(): ResultWrapper<CategoriesListModel> {
        val url = "$baseUrl/categories"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { categories: CategoryListResponse ->
                categories.toCategoryList()
            }
        )
    }

    override suspend fun addProductToCart(request: AddCartRequestModel,userId: Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/${userId}"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = AddToCartRequest.fromCartRequestModel(request),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            }
        )
    }

    override suspend fun getCart(userId: Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            }
        )
    }

    override suspend fun updateQuantity(cartItemModel: CartItemModel,userId: Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId/${cartItemModel.id}"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Put,
            body = AddToCartRequest(
                productId = cartItemModel.productId,
                productName = cartItemModel.productName,
                price = cartItemModel.price,
                quantity = cartItemModel.quantity
            ),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            }
        )
    }

    override suspend fun deleteItem(cartItemId: Int, userId: Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId/$cartItemId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Delete,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            }
        )
    }

    override suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary> {
        val url = "$baseUrl/checkout/$userId/summary"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { cartSummary: CartSummaryResponse ->
                cartSummary.toCartSummary()
            })
    }

    override suspend fun placeOrder(address: AddressDomainModel, userId: Long): ResultWrapper<Long> {
     val dataModel= AddressDataModel.fromDomainAddress(address)
        val url = "$baseUrl/orders/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = dataModel,
            mapper = { orderRes: PlaceOrderResponse ->
                orderRes.data.id
            })
    }

    override suspend fun getOrdersList(userId: Long): ResultWrapper<OrdersListModel> {
        val url = "$baseUrl/orders/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { orderResponse: OrderListResponse ->
                orderResponse.toDomainResponse()
            })
    }

    override suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/login"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = LoginRequest(
               email,
             password
            )
            ,mapper = { user: UserAuthResponse ->
                user.data?.toDomainModel()?:throw Exception("User is null")
            })
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/signup"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = com.pete.data.di.model.request.RegisterRequest(
                email,
                password,
               name
            )
            ,mapper = { user: UserAuthResponse ->
                user.data?.toDomainModel()?:throw Exception("User is null")
            })
    }

    private suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String>? = null,
        parameter: Map<String, String>? = emptyMap(),
        noinline mapper: ((T) -> R)? = null
    ): ResultWrapper<R> {
        return try {
            val response = client.request(url) {
                this.method = method

                // Apply query parameters
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameter?.forEach { (key, value) ->
                            append(key, value)
                        }
                    })
                }

                // Apply headers
                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                // Set body if present
                if (body != null) {
                    setBody(body)
                }

                contentType(ContentType.Application.Json)
            }.body<T>()

            val result = mapper?.invoke(response) ?: response as R
            ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            ResultWrapper.Failure(Exception("Client error: ${e.message}"))
        } catch (e: ServerResponseException) {
            ResultWrapper.Failure(Exception("Server error: ${e.message}"))
        } catch (e: IOException) {
            ResultWrapper.Failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            ResultWrapper.Failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}
