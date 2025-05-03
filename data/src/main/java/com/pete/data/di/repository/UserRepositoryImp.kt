package com.pete.data.di.repository

import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.repository.UserRepository

class UserRepositoryImp (private val networkService: NetworkService): UserRepository{
    override suspend fun login(email: String, password: String) =
        networkService.login(email, password)
    override suspend fun register(email: String, password: String, name: String) =
        networkService.register(email, password, name)
}
