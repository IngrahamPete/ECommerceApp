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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pete.domain.di.model.CartItemModel
import com.pete.ecommerceapp.R
import com.pete.ecommerceapp.navigation.CartSummaryScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    val removingItemId = viewModel.removingItemId.collectAsState()
    val cartItems = remember { mutableStateOf(emptyList<CartItemModel>()) }
    val loading = remember { mutableStateOf(false) }
    val errorMsg = remember { mutableStateOf<String?>(null) }
    val showDeleteDialog = remember { mutableStateOf<CartItemModel?>(null) }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is CartEvent.Loading -> {
                loading.value = true
                errorMsg.value = null
            }
            is CartEvent.Error -> {
                loading.value = false
                errorMsg.value = (uiState.value as CartEvent.Error).message
            }
            is CartEvent.Success -> {
                loading.value = false
                val data = (uiState.value as CartEvent.Success).message
                cartItems.value = data
                if (data.isEmpty()) {
                    errorMsg.value = "No items in cart"
                }
            }
        }
    }

    if (showDeleteDialog.value != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog.value = null },
            title = { Text("Remove Item") },
            text = { Text("Are you sure you want to remove this item from your cart?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog.value?.let { item ->
                            viewModel.removeItem(item)
                            showDeleteDialog.value = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.getCart()
            delay(500)
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_basic_svgrepo_com),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                PullToRefreshContainer(
                    state = pullToRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(pullToRefreshState.nestedScrollConnection)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        loading.value -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Loading cart...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        errorMsg.value != null -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = errorMsg.value ?: "Something went wrong!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Button(
                                    onClick = { viewModel.getCart() }
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                        cartItems.value.isNotEmpty() -> {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cartItems.value, key = { it.id }) { item ->
                                    CartItem(
                                        item = item,
                                        onIncrement = { viewModel.incrementQuantity(it) },
                                        onDecrement = { viewModel.decrementQuantity(it) },
                                        onRemove = { showDeleteDialog.value = it },
                                        isRemoving = removingItemId.value == item.id
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate(CartSummaryScreen) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Proceed to Checkout",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
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
    onRemove: (CartItemModel) -> Unit,
    isRemoving: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${item.price}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onDecrement(item) },
                            enabled = !isRemoving,
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.minus),
                                contentDescription = "Decrease",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = item.quantity.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        IconButton(
                            onClick = { onIncrement(item) },
                            enabled = !isRemoving,
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_add_24),
                                contentDescription = "Increase",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { onRemove(item) },
                        enabled = !isRemoving,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        if (isRemoving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.trash_can),
                                contentDescription = "Remove",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}


