package com.julia.iwatch.login

import com.julia.iwatch.auth.LoginResponse
import com.julia.iwatch.auth.UserCredentials
import com.julia.iwatch.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun login(credentials: UserCredentials): LoginResponse {
        val response = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }

        return response.body()
    }
}