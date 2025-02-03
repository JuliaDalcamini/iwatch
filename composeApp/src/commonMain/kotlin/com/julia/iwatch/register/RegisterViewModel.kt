package com.julia.iwatch.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.iwatch.auth.UserCredentials
import kotlinx.coroutines.launch

/**
 * View model for the register screen.
 */
class RegisterViewModel(
    private val repository: RegisterRepository = RegisterRepository()
) : ViewModel() {

    /**
     * The current state of the login screen.
     */
    var uiState by mutableStateOf(RegisterUiState())
        private set

    /**
     * Attempts to register a new user.
     */
    fun register() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, loading = true)

            try {
                val response = repository.register(
                    RegisterRequest(
                        uiState.firstName,
                        uiState.lastName,
                        UserCredentials(
                            uiState.email,
                            uiState.password
                        )
                    )
                )

            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Sets the typed first name.
     */
    fun setFirstName(firstName: String) {
        uiState = uiState.copy(firstName = firstName)
    }

    /**
     * Sets the typed last name.
     */
    fun setLastName(lastName: String) {
        uiState = uiState.copy(lastName = lastName)
    }

    /**
     * Sets the typed email.
     */
    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    /**
     * Sets the typed password.
     */
    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    /**
     * Sets the typed password confirmation.
     */
    fun setPasswordConfirmation(passwordConfirmation: String) {
        uiState = uiState.copy(passwordConfirmation = passwordConfirmation)
        validatePasswords()
    }

    /**
     * Dismisses the error dialog.
     */
    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }

    /**
     * Confirmation if passwords are match.
     */
    private fun validatePasswords() {
        val mismatch = uiState.passwordConfirmation.isNotEmpty() &&
                uiState.password.isNotEmpty() &&
                uiState.password != uiState.passwordConfirmation

        uiState = uiState.copy(passwordMismatch = mismatch)
    }
}