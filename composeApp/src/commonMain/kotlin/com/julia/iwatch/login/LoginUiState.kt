package com.julia.iwatch.login

import com.julia.iwatch.auth.UserCredentials

/**
 * Login screen state.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val loading: Boolean = false,
    val navigateToHome: Boolean = false
)

/**
 * The currently entered credentials.
 */
val LoginUiState.typedCredentials
    get() = UserCredentials(email, password)