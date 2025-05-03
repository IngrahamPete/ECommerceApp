package com.pete.domain.di.usecase

import com.pete.domain.di.repository.UserRepository

class RegisterUseCase (private val userRepository: UserRepository){
    suspend fun execute(username:String,password:String,name:String)=
        userRepository.register(username,password,name)

}