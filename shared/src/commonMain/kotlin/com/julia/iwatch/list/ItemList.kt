package com.julia.iwatch.list

import kotlinx.serialization.Serializable

@Serializable
data class ItemList(
    val id: String,
    val name: String,
    val userId: String,
    val itemCount: Int
)