package com.julia.iwatch.item

import kotlinx.serialization.Serializable

@Serializable
data class UpdateItemRequest(val watched: Boolean)