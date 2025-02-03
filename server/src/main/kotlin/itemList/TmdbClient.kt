package itemList

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class AuthenticationResponse(
    val success: Boolean,
    val expires_at: String? = null,
    val request_token: String? = null
)

class TmdbClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun authenticate(): AuthenticationResponse {
        return client.get("https://api.themoviedb.org/3/authentication") {
            headers {
                append("accept", "application/json")
                append(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjOWI5YTE0MTBlNzRlMzk3MmYyYTRhMjMwZWUxOWVkMCIsIm5iZiI6MTczODE3OTYyOC4zMTIsInN1YiI6IjY3OWE4NDJjODdlMDgwNGVjM2JkNmEwZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.M-CKyf3MACjeTJ9D3OJpfnAbG3FPoAJaJU9wUrdRGEk"
                )
            }
        }.body()
    }
}