package com.julia.iwatch.common.navigation

import androidx.navigation.NavController

inline fun <reified T : Any> NavController.popUpToAndNavigate(route: T) =
    this.navigate(route) {
        popUpTo<T> { inclusive = true }
    }