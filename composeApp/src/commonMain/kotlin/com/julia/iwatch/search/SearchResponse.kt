package com.julia.iwatch.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("results") val results: List<SearchResult>
)
