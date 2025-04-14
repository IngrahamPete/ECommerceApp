package com.pete.ecommerceapp.di

import com.pete.ecommerceapp.ui.theme.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get())
    }
    //viewModelOf(::HomeViewModel)

       /* @Composable
        fun HomeScreen() {
            val vm=koinViewModel<HomeViewModel>()

        }
*/


}