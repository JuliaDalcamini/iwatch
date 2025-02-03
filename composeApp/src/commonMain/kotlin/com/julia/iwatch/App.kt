package com.julia.iwatch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.julia.iwatch.login.LoginRoute
import com.julia.iwatch.login.LoginScreen
import com.julia.iwatch.register.RegisterRoute
import com.julia.iwatch.register.RegisterScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = LoginRoute()
        ) {
            composable<LoginRoute> { entry ->
                val route = entry.toRoute<LoginRoute>()

                LoginScreen(
                    presetEmail = route.email,
                    presetPassword = route.password,
                    onSignIn = { },
                    onRegisterClick = { userCredentials ->
                        navController.navigate(RegisterRoute.from(userCredentials))
                    }
                )
            }

            composable<RegisterRoute> { entry ->
                val route = entry.toRoute<RegisterRoute>()

                RegisterScreen(
                    presetEmail = route.email,
                    presetPassword = route.password,
                    onLoginClick = { navController.popBackStack() },
                    onRegistrationComplete = { userCredentials ->
                        navController.navigate(LoginRoute.from(userCredentials)) {
                            popUpTo<RegisterRoute> {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}