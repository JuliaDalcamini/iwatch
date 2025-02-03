package plugins

import auth.authRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import itemList.item.itemRoutes
import itemList.itemListRoutes
import user.userRoutes

fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()
        itemRoutes()
        itemListRoutes()
    }
}
