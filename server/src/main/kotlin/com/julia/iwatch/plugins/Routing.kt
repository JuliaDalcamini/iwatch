package com.julia.iwatch.plugins

import com.julia.iwatch.auth.authRoutes
import com.julia.iwatch.item.itemRoutes
import com.julia.iwatch.list.listRoutes
import com.julia.iwatch.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()
        listRoutes()
        itemRoutes()
    }
}
