package com.pete.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pete.ecommerceapp.ui.theme.ECommerceAppTheme
import com.pete.ecommerceapp.ui.theme.feature.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceAppTheme {
                val navController = rememberNavController()
                NavHost(navController=navController, startDestination = "home") {
                    composable("home"){
                        HomeScreen(navController)
                    }
                }
            }
        }
    }
}
