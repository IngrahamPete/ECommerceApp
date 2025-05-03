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
fun SearchBar(value:String,onTextChange:(String)->Unit) {
    TextField(
        value = value,
        onValueChange = onTextChange,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        leadingIcon =
            {
                Image( painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp))
            }
        , colors = TextFieldDefaults.
        colors(focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent, focusedContainerColor = Color.LightGray.copy(alpha=0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
    ),
        placeholder ={
        Text(
            text = "Search for Products",
            style = MaterialTheme.typography.bodySmall
        )
    }
    )

}



@Composable
fun ProfileHeader(navController: NavController)
{
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp
    ))
    {
        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart
            ))
        {
            Image(painter = painterResource(id= R.drawable.ic_launcher_foreground), contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Hello",
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = "John Doe",
                    style = MaterialTheme.typography.titleMedium
                , fontWeight = FontWeight.SemiBold
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.baseline_notifications_24), contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd)
                .size(48.dp)
                .padding(8.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha=0.3f))
                .clickable { navController.navigate(CartScreen) },
            contentScale = ContentScale.Inside
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
                            Text(text = category.replaceFirstChar { it.uppercase() } ,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.
                                padding(horizontal = 8.dp).
                                clip(RoundedCornerShape(8.dp)).
                                background(MaterialTheme.colorScheme.primary).
                                padding(8.dp)
                            )

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
fun HomeProductRow(products:List<Product>,title:String,onClick: (Product) -> Unit)
{
    Column {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(
                    Alignment.CenterStart
                ),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
                ,
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow {
            items(products, key = {it.id}) { products ->
                val isVisible= remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = true){
                    isVisible.value=true

                }
                AnimatedVisibility(visible =isVisible.value,enter= fadeIn()+ expandVertically())
                {
                    ProductItem(product = products, onClick)
                }

            }
        }
    }
}


@Composable
fun ProductItem(product: Product,onClick:(Product)->Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp)
            .size(width = 126.dp, height = 144.dp).clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(contentColor = Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(96.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                color= MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}


