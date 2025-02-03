import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.cio.EngineMain
import io.ktor.server.engine.embeddedServer
import plugins.configureDependencyInjection
import plugins.configureHTTP
import plugins.configureMonitoring
import plugins.configureRouting
import plugins.configureSecurity
import plugins.configureSerialization

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}


fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
