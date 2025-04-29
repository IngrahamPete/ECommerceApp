package com.pete.data.di

import android.util.Log
import com.pete.data.di.network.NetworkServiceImp
import com.pete.domain.di.network.NetworkService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

//provides HTTpClient and binds it to NetworkService
val networkModule = module {
    single {
        HttpClient(CIO) {/*A multiplatform asynchronous HTTP client,
         which allows you to make requests and handle responses,
         extend its functionality with plugins, such as authentication,
         JSON serialization, and so on.*/
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("NETWORK", message)
                    }
                }
            }
        }
    }
    single<NetworkService> {
        NetworkServiceImp(
            client = get(),
            baseUrl = "https://ecommerce-ktor-4641e7ff1b63.herokuapp.com/v2"
        )
    }
}
