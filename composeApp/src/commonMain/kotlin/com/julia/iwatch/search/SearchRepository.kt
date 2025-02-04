package com.julia.iwatch.search

import com.julia.iwatch.tmdb.tmdbClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Repository for searching movies.
 */
class SearchRepository(private val client: HttpClient = tmdbClient) {

    suspend fun searchMovies(query: String): List<SearchResult> =
        client.get("search/movie") {
            parameter("query", query)
            parameter("language", "pt-BR")
            parameter("region", "BR")
        }.body<SearchResponse>().results
}