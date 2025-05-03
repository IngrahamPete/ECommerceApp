package com.pete.ecommerceapp.ui.feature.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.usecase.LoginUsecase
import com.pete.ecommerceapp.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase:LoginUsecase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope. launch {
            val response = loginUseCase.execute(email, password)
            when (response) {
                is ResultWrapper.Success -> {
                    ShopperSession.storeUser(response.value)
                    _loginState.value = LoginState.Success()
                }

                is ResultWrapper.Failure -> {
                    _loginState.value = LoginState.Error(
                        response.exception.message
                            ?: "Something went wrong!"
                    )
                }
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    class Success : LoginState()
    data class Error(val message: String) : LoginState()
}