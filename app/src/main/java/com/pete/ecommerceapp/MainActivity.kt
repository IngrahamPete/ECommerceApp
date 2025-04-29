package com.pete.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pete.ecommerceapp.model.UiProductModel
import com.pete.ecommerceapp.navigation.CartScreen
import com.pete.ecommerceapp.navigation.CartSummaryScreen
import com.pete.ecommerceapp.navigation.HomeScreen
import com.pete.ecommerceapp.navigation.ProductDetails
import com.pete.ecommerceapp.navigation.ProfileScreen
import com.pete.ecommerceapp.navigation.productNavType
import com.pete.ecommerceapp.ui.feature.cart.CartScreen
import com.pete.ecommerceapp.ui.feature.home.HomeScreen
import com.pete.ecommerceapp.ui.feature.product_details.ProductDetailsScreen
import com.pete.ecommerceapp.ui.feature.summary.CartSummaryScreen
import com.pete.ecommerceapp.ui.theme.ECommerceAppTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val shouldShowFab = remember {
                mutableStateOf(true)
            }
            ECommerceAppTheme {
                val shouldShowBottomNav = remember {
                    mutableStateOf(true)
                }
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn()) {
                            BottomNavigationBar(navController)
                        }
                    }

                ) {
                    Surface(modifier = Modifier.padding(it)) { }
                    NavHost(navController = navController, startDestination = HomeScreen) {
                        composable<HomeScreen> {
                            shouldShowBottomNav.value = true
                            shouldShowFab.value = true
                            HomeScreen(navController)
                        }
                        composable<CartScreen> {
                            shouldShowBottomNav.value = true
                            shouldShowFab.value = false
                            CartScreen(navController)
                        }
                        composable<ProfileScreen> {
                            shouldShowBottomNav.value = true
                            shouldShowFab.value = false
                            Text(text = "Cart")
                        }
                        composable<CartSummaryScreen> {
                            shouldShowBottomNav.value = false
                            shouldShowFab.value = false
                            CartSummaryScreen(navController=navController)
                        }
                        composable<ProductDetails>(
                            typeMap = mapOf(typeOf<UiProductModel>() to productNavType)
                        ) {
                            shouldShowBottomNav.value = false
                            shouldShowFab.value = false
                            val productRoute = it.toRoute<ProductDetails>()
                            ProductDetailsScreen(navController, productRoute.product)
                        }
                    }
                }
            }
        }
    }

@Composable
fun BottomNavigationBar(navController: NavController){
    NavigationBar {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val items= listOf(
            BottomNavItems.Home,
            BottomNavItems.Cart,
            BottomNavItems.Profile
        )
        items.forEach {
            item->
            val isSelected =  currentRoute?.substringBefore('?')==item.route::class.qualifiedName
            NavigationBarItem(
                selected =isSelected,
                onClick = { navController.navigate(item.route){
                    popUpTo(navController.graph.startDestinationId){
                        saveState=true
                    }
                    launchSingleTop=true
                    restoreState=true
                } },
                label = { Text(text = item.title)  },
                icon = {
                    Image(
                        painter = painterResource(id = item.icon),
                    contentDescription = null,
                        colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray))

                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                   unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray

                )

            )
        }


    }

}

//Used in navroutes to change pages in the app
sealed class BottomNavItems(val route:Any,val title:String,val icon:Int)
{
    data object Home:BottomNavItems(HomeScreen,"Home", icon=R.drawable.baseline_home_24)
    data object Cart:BottomNavItems(CartScreen,"Cart", icon=R.drawable.baseline_shopping_cart_24)
    data object Profile:BottomNavItems(ProfileScreen,"Profile", icon=R.drawable.baseline_person_24)
}}
