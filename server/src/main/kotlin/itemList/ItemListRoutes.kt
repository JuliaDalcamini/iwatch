package itemList

import auth.authenticatedUserId
import com.julia.iwatch.auth.refresh.RefreshTokensRequest
import com.julia.iwatch.login.LoginRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.itemListRoutes() {
    val service by inject<ItemListService>()

    route("itemlist/") {
        authenticate {

            post("create") {
                val response = service.createItemList(
                    request = call.receive<ItemListRequest>(),
                    loggedUserId = call.authenticatedUserId
                )
                call.respond(response)
            }

            patch("{id}") {
                val itemList = service.updateItemList(
                    request = call.receive<ItemListRequest>(),
                    idItemList = call.parameters["id"] ?: throw BadRequestException("Missing item list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(itemList)
            }

            get("{id}") {
                val itemLists = service.getAll(
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(itemLists)
            }

            delete("{id}") {
                service.deleteItemList(
                    idItemList = call.parameters["id"] ?: throw BadRequestException("Missing item list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}