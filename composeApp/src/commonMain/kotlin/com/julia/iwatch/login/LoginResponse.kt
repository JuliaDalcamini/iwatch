package com.julia.iwatch.login

import com.julia.iwatch.auth.TokenPair
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: String,
    val tokens: TokenPair
)
