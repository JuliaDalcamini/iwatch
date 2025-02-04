package com.julia.iwatch

import com.julia.iwatch.plugins.configureDependencyInjection
import com.julia.iwatch.plugins.configureHTTP
import com.julia.iwatch.plugins.configureMonitoring
import com.julia.iwatch.plugins.configureRouting
import com.julia.iwatch.plugins.configureSecurity
import com.julia.iwatch.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}


fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
