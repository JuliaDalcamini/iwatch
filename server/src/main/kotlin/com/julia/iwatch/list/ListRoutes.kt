package com.julia.iwatch.list

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

fun Route.listRoutes() {
    val service by inject<ListService>()

    route("lists") {
        authenticate {
            get {
                val response = service.getAll(call.authenticatedUserId)
                call.respond(response)
            }

            post {
                val response = service.create(
                    request = call.receive<ItemListRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(response)
            }

            patch("{id}") {
                val itemList = service.update(
                    request = call.receive<ItemListRequest>(),
                    listId = call.parameters["id"] ?: throw BadRequestException("Missing item list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(itemList)
            }

            get("{id}") {
                val itemLists = service.get(
                    listId = call.parameters["id"] ?: throw BadRequestException("Missing item list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(itemLists)
            }

            delete("{id}") {
                service.delete(
                    listId = call.parameters["id"] ?: throw BadRequestException("Missing item list ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}