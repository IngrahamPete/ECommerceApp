package com.pete.ecommerceapp.ui.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.usecase.DeleteProductUSeCase
import com.pete.domain.di.usecase.GetCartUseCase
import com.pete.domain.di.usecase.UpdateQuantityUSeCase
import com.pete.ecommerceapp.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    val cartUseCase: GetCartUseCase,
    private val updateQuantityUSeCase: UpdateQuantityUSeCase,
    private val deleteItem: DeleteProductUSeCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartEvent>(CartEvent.Loading)
    val uiState = _uiState.asStateFlow()
    val userDomainModel=ShopperSession.getUser()
    private val _removingItemId = MutableStateFlow<Int?>(null)
    val removingItemId = _removingItemId.asStateFlow()

    init {
        getCart()
    }

    fun getCart() {
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            cartUseCase.execute(userDomainModel!!.id!!.toLong()).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiState.value = CartEvent.Success(result.value.data)
                    }
                    is ResultWrapper.Failure -> {
                        _uiState.value = CartEvent.Error("Failed to load cart items")
                    }
                }
            }
        }
    }

    fun updateQuantity(cartItem: CartItemModel) {
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            val result = updateQuantityUSeCase.execute(cartItem,userDomainModel!!.id!!.toLong())
            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartEvent.Success(result.value.data)
                }
                is ResultWrapper.Failure -> {
                    _uiState.value = CartEvent.Error("Failed to update quantity")
                }
            }
        }
    }

    fun incrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 10) return
        updateQuantity(cartItem.copy(quantity = cartItem.quantity + 1))
    }

    fun decrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 1) return
        updateQuantity(cartItem.copy(quantity = cartItem.quantity - 1))
    }

    fun removeItem(cartItem: CartItemModel) {
        viewModelScope.launch {
            _removingItemId.value = cartItem.id
            _uiState.value = CartEvent.Loading
            val result = deleteItem.execute(cartItem.id, userId = 1)
            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartEvent.Success(result.value.data)
                }
                is ResultWrapper.Failure -> {
                    _uiState.value = CartEvent.Error("Failed to remove item from cart")
                }
            }
            _removingItemId.value = null
        }
    }
}

sealed class CartEvent {
    data object Loading : CartEvent()
    data class Success(val message: List<CartItemModel>) : CartEvent()
    data class Error(val message: String) : CartEvent()
}