package user

import com.julia.iwatch.auth.RegisterRequest
import common.inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.userRoutes() {
    val service by inject<UserService>()

    post("register") {
        service.register(request = call.receive<RegisterRequest>())
        call.respond(HttpStatusCode.Created)
    }
}