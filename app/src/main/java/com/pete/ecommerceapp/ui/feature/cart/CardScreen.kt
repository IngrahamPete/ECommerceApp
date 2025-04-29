package com.pete.ecommerceapp.ui.feature.cart
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pete.domain.di.model.CartItemModel
import com.pete.ecommerceapp.R
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = koinViewModel()
) {
    // collect your ui state
    val uiState by viewModel.uiState.collectAsState()
    // derive local flags
    val isLoading = uiState is CartEvent.Loading
    val errorMsg = (uiState as? CartEvent.Error)?.message
    val items = (uiState as? CartEvent.Success)?.message.orEmpty()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cart") })
        },
        bottomBar = {
            // only show when we have items and no error/loading
            if (items.isNotEmpty() && !isLoading && errorMsg == null) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Checkout")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                errorMsg != null -> {
                    Text(
                        text = errorMsg,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(items) { item ->
                            CartItem(
                                item = item,
                                onIncrement = { viewModel.incrementQuantity(it) },
                                onDecrement = { viewModel.decrementQuantity(it) },
                                onRemove = { viewModel.removeItem(it) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(
    item: CartItemModel,
    onIncrement: (CartItemModel) -> Unit,
    onDecrement: (CartItemModel) -> Unit,
    onRemove: (CartItemModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.productName,
            modifier = Modifier.size(126.dp, 96.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = { onRemove(item)}) {
                Icon(painterResource(R.drawable.trash_can), contentDescription = "Remove")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onDecrement(item) }) {
                    Icon(painterResource(R.drawable.minus), contentDescription = "Decrease")
                }
                Text(item.quantity.toString())
                IconButton(onClick = { onIncrement(item) }) {
                    Icon(painterResource(R.drawable.baseline_add_24), contentDescription = "Increase")
                }
            }
        }
    }
}
