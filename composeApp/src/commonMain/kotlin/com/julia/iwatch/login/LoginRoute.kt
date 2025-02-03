package com.julia.iwatch.login

import com.julia.iwatch.auth.UserCredentials
import kotlinx.serialization.Serializable

/**
 * Route for the login screen.
 */
@Serializable
data class LoginRoute(
    val email: String? = null,
    val password: String? = null
) {

    companion object {

        /**
         * Creates a login route from the given user credentials.
         */
        fun from(userCredentials: UserCredentials?) =
            LoginRoute(
                email = userCredentials?.email,
                password = userCredentials?.password
            )
    }
}