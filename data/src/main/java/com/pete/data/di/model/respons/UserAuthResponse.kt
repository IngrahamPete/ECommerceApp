package com.pete.data.di.model.respons

import kotlinx.serialization.Serializable


@Serializable
data class UserAuthResponse (
    val data:UserResponse ?=null,
    val msg:String
)