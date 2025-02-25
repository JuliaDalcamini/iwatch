package com.julia.iwatch.common.network

import com.julia.iwatch.auth.TokenPair
import com.julia.iwatch.auth.refresh.RefreshTokensRequest
import com.julia.iwatch.auth.toBearerTokens
import com.julia.iwatch.common.logging.createLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val API_BASE_URL = "http://localhost:8080/"
private var authTokens: TokenPair? = null

expect fun getClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig>

/**
 * Get an already configured HttpClient.
 */
val configuredHttpClient: HttpClient by lazy {
    HttpClient(getClientEngine()) {
        expectSuccess = true

        install(DefaultRequest) {
            url(API_BASE_URL)
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

        install(Auth) {
            bearer {
                loadTokens {
                    authTokens?.toBearerTokens()
                }

                refreshTokens {
                    oldTokens?.let {
                        authTokens = client.post("refresh_tokens") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokensRequest(it.refreshToken.orEmpty()))
                        }.body<TokenPair>()

                        authTokens?.toBearerTokens()
                    }
                }
            }
        }

        install(Logging) {
            logger = createLogger("HttpClient")
            level = LogLevel.BODY
        }
    }
}

private fun invalidateBearerTokens() {
    configuredHttpClient.authProviders
        .filterIsInstance<BearerAuthProvider>()
        .singleOrNull()
        ?.clearToken()
}

/**
 * Clear the auth tokens.
 */
fun clearAuthTokens() {
    authTokens = null
    invalidateBearerTokens()
}

/**
 * Set the auth tokens.
 */
fun setAuthTokens(tokens: TokenPair) {
    authTokens = tokens
    invalidateBearerTokens()
}
