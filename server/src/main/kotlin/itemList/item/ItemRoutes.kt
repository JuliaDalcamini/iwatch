package itemList.item

import auth.authenticatedUserId
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

fun Route.itemRoutes() {
    val service by inject<ItemService>()

    route("itemlist/item/") {
        authenticate {

            post("create") {
                val response = service.addItem(
                    request = call.receive<ItemRequest>(),
                    loggedUserId = call.authenticatedUserId
                )
                call.respond(response)
            }

            patch("{id}") {
                val item = service.updateItem(
                    idItem = call.parameters["id"] ?: throw BadRequestException("Missing item ID"),
                    request = call.receive<ItemRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(item)
            }

            get("{id}") {
                val items = service.getItemsFromList(
                    itemListId = call.parameters["id"] ?: throw BadRequestException("Missing item ID")
                )

                call.respond(items)
            }

            delete("{id}") {
                service.deleteItem(
                    idItem = call.parameters["id"] ?: throw BadRequestException("Missing item ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}