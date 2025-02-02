package com.julia.iwatch.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokensRequest(
    val refreshToken: String
)