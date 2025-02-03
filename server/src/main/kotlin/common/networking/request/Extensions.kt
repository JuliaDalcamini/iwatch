package common.networking.request

import io.ktor.server.application.ApplicationCall

val ApplicationCall.query
    get() = this.request.queryParameters