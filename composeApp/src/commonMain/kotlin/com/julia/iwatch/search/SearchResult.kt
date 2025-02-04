package com.julia.iwatch.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String?
)
