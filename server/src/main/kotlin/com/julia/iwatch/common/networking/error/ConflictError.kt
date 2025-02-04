package com.julia.iwatch.common.networking.error

import io.ktor.http.HttpStatusCode

class ConflictError(message: String) : HttpError(HttpStatusCode.Conflict, message)