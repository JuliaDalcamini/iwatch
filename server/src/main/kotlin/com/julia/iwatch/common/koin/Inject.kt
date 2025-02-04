package com.julia.iwatch.common.koin

import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import org.koin.ktor.ext.inject

/**
 * Injects value from application context. Needed to prevent a bug from Koin.
 */
inline fun <reified T : Any> Route.inject(): Lazy<T> =
    application.inject<T>()