package com.julia.iwatch.register

import com.julia.iwatch.auth.RegisterRequest
import com.julia.iwatch.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RegisterRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun register(registerRequest: RegisterRequest) {
        val response = client.post("register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }

        return response.body()
    }
}