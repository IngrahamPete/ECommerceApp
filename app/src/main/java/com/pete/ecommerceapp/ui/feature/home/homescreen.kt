package com.pete.ecommerceapp.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pete.domain.di.model.Product
import com.pete.ecommerceapp.R
import com.pete.ecommerceapp.ShopperSession
import com.pete.ecommerceapp.model.UiProductModel
import com.pete.ecommerceapp.navigation.CartScreen
import com.pete.ecommerceapp.navigation.ProductDetails
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    val loading = remember{
        mutableStateOf(false)
    }
    val error = remember{
        mutableStateOf<String?>(null)
    }
    val featured = remember {
        mutableStateOf<List<Product>>(emptyList())
    }
    val popularProducts = remember {
        mutableStateOf<List<Product>>(emptyList())
    }
    val categories = remember {
        mutableStateOf<List<String>>(emptyList())
    }
    Scaffold {
        Surface(modifier = Modifier.fillMaxSize().padding(it))
        {
            when (uiState.value) {
                is HomeScreenUIEvents.Loading -> {
                    loading.value = true
                    error.value=null
                }
                is HomeScreenUIEvents.Success -> {
                    val data = (uiState.value as HomeScreenUIEvents.Success)
                    featured.value = data.featured
                    popularProducts.value = data.popularProducts
                    categories.value = data.categories

                    loading.value = false
                    error.value=null
                }
                is HomeScreenUIEvents.Error -> {
                    val errorMessage = (uiState.value as HomeScreenUIEvents.Error).message
                    loading.value = false
                    error.value =errorMessage
                }
            }
            HomeContent(featured.value,
                popularProducts.value,
                categories.value,
                loading.value,
                error.value, onClick = {
                    navController.navigate(ProductDetails(UiProductModel.fromProduct(it)))
                }, navController)
        }
    }
}

@Composable
fun SearchBar(value: String, onTextChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        placeholder = {
            Text(
                text = "Search for Products",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
fun ProfileHeader(navController: NavController) {
    val user = remember { ShopperSession.getUser() }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user?.username?.firstOrNull()?.uppercase() ?: "G",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user?.username ?: "Guest",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { navController.navigate(CartScreen) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_shopping_cart_24),
                    contentDescription = "Cart",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = category.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun HomeContent(featured:List<Product>,
                popularProduct: List<Product>,
                categories:List<String>,isLoading:Boolean=false,
                errorMsg:String?=null,onClick: (Product) -> Unit, navController: NavController)
{
    LazyColumn {
        item {
            ProfileHeader(navController)
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(value = "", onTextChange ={} )
            Spacer(modifier = Modifier.size(16.dp))
         }
        item{
            if (isLoading) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
            errorMsg?.let {  Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            if (categories.isNotEmpty())
                LazyRow (Modifier.fillMaxWidth()){
                    /*Layout animation*/
                    items(categories, key={it})
                    {category->
                        val isVisible= remember {
                            mutableStateOf(false)
                        }
                        LaunchedEffect(key1 = true){
                            isVisible.value=true

                        }
                        AnimatedVisibility(visible =isVisible.value,enter= fadeIn()+ expandVertically())
                        //animation end
                        {
                            CategoryChip(category)
                        }

                    }

                }
            Spacer(modifier = Modifier.size(16.dp))

            if (featured.isNotEmpty())
            {
                HomeProductRow(products = featured, title = "Featured",onClick)
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (popularProduct.isNotEmpty())
            {
                HomeProductRow(products = popularProduct,title="Popular Products",onClick=onClick)
            }
        }
    }
}

@Composable
fun HomeProductRow(products: List<Product>, title: String, onClick: (Product) -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(products) { product ->
                ProductItem(product, onClick)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(width = 160.dp, height = 200.dp)
            .clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground),
                    alignment = Alignment.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}


