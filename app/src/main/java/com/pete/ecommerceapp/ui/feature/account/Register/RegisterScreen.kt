package com.pete.ecommerceapp.ui.feature.account.Register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pete.ecommerceapp.navigation.HomeScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = koinViewModel()) {

    val loginState = viewModel.loginState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = loginState.value) {
            is RegisterState.Success -> {
                LaunchedEffect(loginState.value) {
                    navController.navigate(HomeScreen) {
                        popUpTo(HomeScreen) {
                            inclusive = true
                        }
                    }
                }
            }

            is RegisterState.Error -> {
                Text(text = state.message, modifier = Modifier.testTag("errorMsg"))
                // Show error message
            }

            is RegisterState.Loading -> {
                CircularProgressIndicator()
                Text(text = "Loading")
            }

            else -> {
                RegisterContent(onRegisterClicked = { email, password,name ->
                    viewModel.register(email, password, name )
                },
                    onSignInCliClick = {
                        navController.popBackStack()
                    })
            }
        }
    }
}


@Composable
fun RegisterContent(onRegisterClicked: (String, String,String) -> Unit, onSignInCliClick: () -> Unit) {
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
                .testTag("NameField"),
            label = { Text(text = "Name") }
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
                .testTag("emailField"),
            label = { Text(text = "Email") }
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .testTag("passwordField"),
            label = { Text(text="Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                onRegisterClicked(email.value, password.value,name.value)
            }, modifier = Modifier.fillMaxWidth()
                .testTag("loginButton"),
            enabled = email.value.isNotEmpty() && password.value.isNotEmpty() && name.value.isNotEmpty()
        ) {
            Text(text ="Sign Up")
        }
        Text(text = "Already Have an account? Login", modifier = Modifier
            .padding(8.dp)
            .clickable {
                onSignInCliClick()
            })
    }
}

