package com.julia.iwatch.item

import com.julia.iwatch.auth.authenticatedUserId
import com.julia.iwatch.common.koin.inject
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

fun Route.itemRoutes() {
    val service by inject<ItemService>()

    route("lists/{listId}/items") {
        authenticate {
            post {
                val response = service.addItem(
                    request = call.receive<CreateItemRequest>(),
                    listId = call.parameters["listId"] ?: throw BadRequestException("Missing list ID"),
                    loggedUserId = call.authenticatedUserId
                )
                call.respond(response)
            }

            patch("{id}") {
                val item = service.updateItem(
                    id = call.parameters["id"] ?: throw BadRequestException("Missing item ID"),
                    listId = call.parameters["listId"] ?: throw BadRequestException("Missing list ID"),
                    request = call.receive<UpdateItemRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(item)
            }

            get("{id}") {
                val items = service.getAll(
                    listId = call.parameters["listId"] ?: throw BadRequestException("Missing list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(items)
            }

            get {
                val items = service.getAll(
                    listId = call.parameters["listId"] ?: throw BadRequestException("Missing list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(items)
            }

            delete("{id}") {
                service.deleteItem(
                    id = call.parameters["id"] ?: throw BadRequestException("Missing item ID"),
                    listId = call.parameters["listId"] ?: throw BadRequestException("Missing list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}