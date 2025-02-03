package common

import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import org.koin.ktor.ext.inject

inline fun <reified T : Any> Route.inject(): Lazy<T> =
    application.inject<T>()