package com.julia.iwatch.register

import com.julia.iwatch.auth.UserCredentials
import kotlinx.serialization.Serializable

/**
 * Route for the register screen.
 */
@Serializable
data class RegisterRoute(
    val email: String? = null,
    val password: String? = null
) {

    companion object {

        /**
         * Creates a register route from the given user credentials.
         */
        fun from(userCredentials: UserCredentials?) =
            RegisterRoute(
                email = userCredentials?.email,
                password = userCredentials?.password
            )
    }
}
