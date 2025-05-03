package com.pete.ecommerceapp.ui.feature.summary
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pete.domain.di.model.CartSummary
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.usecase.CartSummaryUseCase
import com.pete.domain.di.usecase.PlaceOrderUseCase
import com.pete.ecommerceapp.ShopperSession
import com.pete.ecommerceapp.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartSummaryViewModel(
    private val cartSummaryUseCase: CartSummaryUseCase,
   private val placeOrderUseCase: PlaceOrderUseCase,
   /* private val shopperSession: ShopperSession*/
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartSummaryEvent>(CartSummaryEvent.Loading)
    val uiState = _uiState.asStateFlow()
    val userDomainModel = ShopperSession.getUser()

    init {
        getCartSummary(userDomainModel!!.id!!)
    }

    private fun getCartSummary(userId: Int) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            val summary = cartSummaryUseCase.execute(userDomainModel!!.id!!.toLong())
            when (summary) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartSummaryEvent.Success(summary.value)
                }

                is ResultWrapper.Failure -> {
                    _uiState.value = CartSummaryEvent.Error("Something went wrong!")
                }
            }
        }
    }

    public fun placeOrder(userAddress: UserAddress) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            val orderId = placeOrderUseCase.execute(userAddress.toAddressDataModel(),userDomainModel!!.id!!.toLong())
            when (orderId) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartSummaryEvent.PlaceOrder(orderId.value)
                }

                is ResultWrapper.Failure -> {
                    _uiState.value = CartSummaryEvent.Error("Something went wrong!")
                }
            }
        }
    }
}


    sealed class CartSummaryEvent {
        data object Loading : CartSummaryEvent()
        data class Error(val error: String) : CartSummaryEvent()
        data class Success(val summary: CartSummary) : CartSummaryEvent()
        data class PlaceOrder(val orderId: Long) : CartSummaryEvent()
    }
