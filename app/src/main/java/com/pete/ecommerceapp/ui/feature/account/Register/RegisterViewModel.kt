package com.pete.ecommerceapp.ui.feature.account.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.usecase.RegisterUseCase
import com.pete.ecommerceapp.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
        private val registerUseCase: RegisterUseCase
    ) : ViewModel() {
        private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
        val loginState = _registerState

        fun register(email: String, password: String,name:String) {
            _registerState.value = RegisterState.Loading
            viewModelScope. launch {
                val response = registerUseCase.execute(email, password,name)
                when (response) {
                    is ResultWrapper.Success -> {
                        ShopperSession.storeUser(response.value)
                        _registerState.value = RegisterState.Success()
                    }

                    is ResultWrapper.Failure -> {
                        _registerState.value = RegisterState.Error(
                            response.exception.message
                                ?: "Something went wrong!"
                        )
                    }
                }
            }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        class Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
