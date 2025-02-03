package com.julia.iwatch.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val userCredentials: UserCredentials
)