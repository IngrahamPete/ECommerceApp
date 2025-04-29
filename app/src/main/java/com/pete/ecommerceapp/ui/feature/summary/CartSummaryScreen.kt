package com.pete.ecommerceapp.ui.feature.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pete.domain.di.model.CartItemModel
import com.pete.domain.di.model.CartSummary
import org.koin.androidx.compose.koinViewModel


@Composable
fun CartSummaryScreen(
    navController: NavController, viewModel: CartSummaryViewModel = koinViewModel()
) {
    /*
    val address = remember {
        mutableStateOf<UserAddress?>(null)
    }*/
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)

    ) {
        Text(
            text = "Cart Summary",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
    val uiState = viewModel.uiState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
    when (val event = uiState.value) {
        is CartSummaryViewModel.CartSummaryEvent.Loading -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(text = "Loading", style = MaterialTheme.typography.titleMedium)
            }
        }

        is CartSummaryViewModel.CartSummaryEvent.Success -> {
            CartSummaryScreenContent(cartSummary = event.summary)

        }

        is CartSummaryViewModel.CartSummaryEvent.Error -> {
            Text(text = event.error, style = MaterialTheme.typography.titleMedium)
        }
        else -> {
            Text(text = "Something went wrong", style = MaterialTheme.typography.titleMedium)
        }
    }
}

}

@Composable
fun CartSummaryScreenContent(cartSummary: CartSummary) {
    LazyColumn {
        item {
            Text(text = "Products")
        }
        items(cartSummary.data.items){cartItem->
            ProductRow(cartItem)
        }
        item {
            Text(text = "Summary")
        }
        item {
            AmountRow("Subtotal", cartSummary.data.subtotal)
            AmountRow("Tax", cartSummary.data.tax)
            AmountRow("Shipping", cartSummary.data.shipping)
            AmountRow("Discount", cartSummary.data.total)
            AmountRow("Total", cartSummary.data.total)
        }
        }
    }


@Composable
fun ProductRow(cartItemModel: CartItemModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = cartItemModel.productName, style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Text(text = "$${ cartItemModel.price } x ${cartItemModel.quantity} ${ cartItemModel.price * cartItemModel.quantity}", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun AmountRow(title: String,amount: Double) {
    Row{
        Text(text = title, style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Text(text = amount.toString(), style = MaterialTheme.typography.titleMedium,)
    }
}



@Composable
fun AddressBar(address: String, onClick: () -> Unit) {

}