package io.github.josephsanjaya.kautoservice

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.ServiceLoader

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val services = ServiceLoader.load(MyService::class.java)
    val service = services.firstOrNull()
    val greeting = service?.hello() ?: "No MyService found!"
    log.info(">>> Loaded Service: $greeting")

    routing {
        get("/") {
            call.respondText(sayHello("Ktor") + "\n" + greeting)
        }
    }
}