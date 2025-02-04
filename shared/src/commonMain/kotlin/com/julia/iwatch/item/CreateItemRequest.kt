package com.julia.iwatch.item

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val name: String,
    val year: String?,
    val posterUrl: String?,
    val tmdbId: String
)