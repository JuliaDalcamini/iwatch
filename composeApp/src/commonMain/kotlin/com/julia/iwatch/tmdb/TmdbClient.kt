package com.julia.iwatch.tmdb

import com.julia.iwatch.common.logging.createLogger
import com.julia.iwatch.common.network.getClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"

/**
 * Client configured to consume TMDB APIs.
 */
val tmdbClient: HttpClient by lazy {
    HttpClient(getClientEngine()) {
        expectSuccess = true

        install(DefaultRequest) {
            url(TMDB_BASE_URL)
            url.parameters.append("api_key", "c9b9a1410e74e3972f2a4a230ee19ed0")
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Logging) {
            logger = createLogger("TmdbClient")
            level = LogLevel.BODY
        }
    }
}