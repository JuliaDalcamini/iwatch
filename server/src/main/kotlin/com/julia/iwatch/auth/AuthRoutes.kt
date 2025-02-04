package com.julia.iwatch.auth

import com.julia.iwatch.auth.refresh.RefreshTokensRequest
import com.julia.iwatch.common.koin.inject
import com.julia.iwatch.login.LoginRequest
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoutes() {
    val service by inject<AuthService>()

    post("login") {
        val response = service.login(request = call.receive<LoginRequest>())
        call.respond(response)
    }

    post("refresh_tokens") {
        val tokens = service.refreshTokens(request = call.receive<RefreshTokensRequest>())
        call.respond(tokens)
    }
}