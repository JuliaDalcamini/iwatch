package com.julia.iwatch.auth.refresh

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokensRequest(
    val refreshToken: String
)