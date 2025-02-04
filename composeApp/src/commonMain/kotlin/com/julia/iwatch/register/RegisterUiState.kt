package com.julia.iwatch.register

import com.julia.iwatch.auth.UserCredentials

/**
 * Register screen state.
 */
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val passwordMismatch: Boolean = false,
    val showError: Boolean = false,
    val loading: Boolean = false,
    val registeredCredentials: UserCredentials? = null
)