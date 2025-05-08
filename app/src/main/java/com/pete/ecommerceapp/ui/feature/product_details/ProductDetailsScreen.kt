package com.pete.ecommerceapp.ui.feature.product_details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pete.ecommerceapp.R
import com.pete.ecommerceapp.model.UiProductModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailsScreen(
    navController: NavController,
    product: UiProductModel,
    viewModel: ProductDetailsViewModel = koinViewModel()
) {
    val loading = remember { mutableStateOf(false) }
    val selectedSize = remember { mutableStateOf(0) }
    val isFavorite = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.weight(1f).background(Color.White)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .align(Alignment.TopCenter)
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_360_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { navController.popBackStack() }
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.star_outline), contentDescription = null)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "4.5",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "(10 Reviews)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Controls
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.decreaseQuantity() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Text(
                        text = viewModel.quantity.collectAsState().value.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    IconButton(
                        onClick = { viewModel.increaseQuantity() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Total Price
                Text(
                    text = "Total: $${String.format("%.2f", product.price * viewModel.quantity.collectAsState().value)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.size(24.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                Button(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier.weight(1f),
                    enabled = !loading.value
                ) {
                    Text(text = "Buy Now")
                }
                Spacer(modifier = Modifier.size(20.dp))
                IconButton(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = IconButtonDefaults.iconButtonColors()
                        .copy(containerColor = Color.LightGray.copy(alpha = 0.4f)),
                    enabled = !loading.value
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_shopping_cart_24),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val uiState = viewModel.state.collectAsState()
        LaunchedEffect(uiState.value) {
            when (uiState.value) {
                is ProductDetailsViewModel.ProductDetailsEvent.Loading -> {
                    loading.value = true
                }
                is ProductDetailsViewModel.ProductDetailsEvent.Success -> {
                    loading.value = false
                    Toast.makeText(
                        navController.context,
                        (uiState.value as ProductDetailsViewModel.ProductDetailsEvent.Success).message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ProductDetailsViewModel.ProductDetailsEvent.Error -> {
                    loading.value = false
                    Toast.makeText(
                        navController.context,
                        (uiState.value as ProductDetailsViewModel.ProductDetailsEvent.Error).message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    loading.value = false
                }
            }
        }

        if (loading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Adding to cart...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
fun SizeItem(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(48.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .padding(8.dp)
    ) {
        Text(
            text = size,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}