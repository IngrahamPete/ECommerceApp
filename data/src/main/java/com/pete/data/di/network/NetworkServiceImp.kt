package com.pete.data.di.network

import com.pete.data.di.model.respons.CategoryListResponse
import com.pete.data.di.model.respons.ProductListResponse
import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.model.ProductListModel
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException

class NetworkServiceImp( val client: HttpClient): NetworkService {
    private val baseUrl="https://ecommerce-ktor-4641e7ff1b63.herokuapp.com/"
    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        val url =
            if(category !=null) "$baseUrl/products/category/$category" else "$baseUrl/products"
        return makeWebRequest(
            url =url,
            method = HttpMethod.Get,
            mapper = { dataModels: ProductListResponse ->
                dataModels.toProductList()
            }
        )
    }

    override suspend fun getCategories():ResultWrapper<CategoriesListModel> {
        val url = "$baseUrl/categories"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { categories:CategoryListResponse ->
                categories.toCategoryList()
            }
        )
    }

    @OptIn(InternalAPI::class)
    suspend inline fun <reified T,R> makeWebRequest(
        url:String,
        method:HttpMethod,
        body:Any?=null,
        headers:Map<String,String>?=null,
        parameter:Map<String,String>?= emptyMap(),
        noinline mapper:((T)->R)? =null
    ):ResultWrapper<R> {
        return try {
            val response = client.request(url) {
                this.method = method

                //apply query parameters
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameter?.forEach { (key, value) ->
                            append(key, value)
                        }
                    })
                }
                headers?.forEach { (key, value) ->
                    header(key, value)
                }

                if (body != null) {
                    this.body = body
                }
                contentType(ContentType.Application.Json)
            }.body<T>()
            val result = mapper?.invoke(response) ?: response as R
            return ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            ResultWrapper.Failure(e)
        } catch (e: ServerResponseException) {
            ResultWrapper.Failure(e)
        } catch (e: IOException) {
            ResultWrapper.Failure(e)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }

    }
    }
