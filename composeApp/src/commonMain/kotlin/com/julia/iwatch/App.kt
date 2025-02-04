package com.julia.iwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.julia.iwatch.common.navigation.popUpToAndNavigate
import com.julia.iwatch.common.network.clearAuthTokens
import com.julia.iwatch.item.ItemsRoute
import com.julia.iwatch.item.ItemsScreen
import com.julia.iwatch.list.ListsRoute
import com.julia.iwatch.list.ListsScreen
import com.julia.iwatch.login.LoginRoute
import com.julia.iwatch.login.LoginScreen
import com.julia.iwatch.register.RegisterRoute
import com.julia.iwatch.register.RegisterScreen
import com.julia.iwatch.search.SearchRoute
import com.julia.iwatch.search.SearchScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    val colors = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colors) {
        val navController = rememberNavController()

        NavHost(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            navController = navController,
            startDestination = LoginRoute()
        ) {
            composable<LoginRoute> { entry ->
                val route = entry.toRoute<LoginRoute>()

                LoginScreen(
                    presetEmail = route.email,
                    presetPassword = route.password,
                    onSignIn = {
                        navController.navigate(ListsRoute) {
                            popUpTo<LoginRoute> {
                                inclusive = true
                            }
                        }
                    },
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
                            popUpTo<RegisterRoute> { inclusive = true }
                        }
                    }
                )
            }

            composable<ListsRoute> {
                ListsScreen(
                    onItemListClick = { list -> navController.navigate(ItemsRoute(list)) },
                    onLogoutClick = {
                        clearAuthTokens()

                        navController.navigate(LoginRoute()) {
                            popUpTo<ListsRoute> { inclusive = true }
                        }
                    }
                )
            }

            composable<ItemsRoute>(ItemsRoute.typeMap) { entry ->
                val route = entry.toRoute<ItemsRoute>()

                ItemsScreen(
                    list = route.list,
                    onAddClick = { navController.navigate(SearchRoute(route.list)) },
                    onBackRequest = { navController.popBackStack() }
                )
            }

            composable<SearchRoute>(SearchRoute.typeMap) { entry ->
                val route = entry.toRoute<SearchRoute>()

                SearchScreen(
                    list = route.list,
                    onAddedToList = { navController.popUpToAndNavigate(ItemsRoute(route.list)) },
                    onBackRequest = { navController.popBackStack() }
                )
            }
        }
    }
}