package com.julia.iwatch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julia.iwatch.login.LoginRoute
import com.julia.iwatch.login.LoginScreen
import com.julia.iwatch.register.RegisterRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = LoginRoute
        ) {
            composable<LoginRoute> {
                LoginScreen(
                    onSignIn = { },
                    onRegisterClick = { userCredentials ->
                        navController.navigate(RegisterRoute.from(userCredentials))
                    }
                )
            }
        }
    }
}