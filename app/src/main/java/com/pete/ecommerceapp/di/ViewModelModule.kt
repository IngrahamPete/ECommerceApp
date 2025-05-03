package com.pete.ecommerceapp.di

import com.pete.ecommerceapp.ui.feature.cart.CartViewModel
import com.pete.ecommerceapp.ui.feature.home.HomeViewModel
import com.pete.ecommerceapp.ui.feature.orders.OrderViewModel
import com.pete.ecommerceapp.ui.feature.product_details.ProductDetailsViewModel
import com.pete.ecommerceapp.ui.feature.summary.CartSummaryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        ProductDetailsViewModel(get())
    }

    viewModel {
        CartViewModel(get(),get(),get())
    }
    viewModel {
        CartSummaryViewModel(get(),get())
    }
    viewModel {
        OrderViewModel(get())
    }
    viewModel {
        com.pete.ecommerceapp.ui.feature.account.login.LoginViewModel(get())
    }
    viewModel {
        com.pete.ecommerceapp.ui.feature.account.Register.RegisterViewModel(get())
    }


}