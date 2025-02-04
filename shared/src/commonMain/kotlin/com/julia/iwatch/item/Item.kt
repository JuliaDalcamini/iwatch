package com.julia.iwatch.item

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val name: String,
    val year: String?,
    val posterUrl: String?,
    val watched: Boolean,
    val tmdbId: String,
    val listId: String
)
