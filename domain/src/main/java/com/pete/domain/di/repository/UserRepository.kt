package com.pete.domain.di.repository

import com.pete.domain.di.model.UserDomainModel
import com.pete.domain.di.network.ResultWrapper

interface UserRepository {
    suspend fun register(email: String, password: String,name:String):ResultWrapper<UserDomainModel>
    suspend fun login(email: String, password: String):ResultWrapper<UserDomainModel>

}