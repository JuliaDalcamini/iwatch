package com.julia.iwatch.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }

    install(RequestValidation)
}