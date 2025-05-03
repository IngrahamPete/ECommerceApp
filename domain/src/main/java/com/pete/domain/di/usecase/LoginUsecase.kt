package com.pete.domain.di.usecase

import com.pete.domain.di.repository.UserRepository

class LoginUsecase(private val userRepository: UserRepository) {
    suspend fun execute(username:String,password:String)=
        userRepository.login(username,password)
}