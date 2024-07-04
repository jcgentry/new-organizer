package com.jacagen.organizer

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.getServiceManager
import io.kvision.remote.kvisionInit


@Suppress("unused")
fun Application.main() {
    install(Compression)
    kvisionInit()
    Db.init(environment.config)

    routing {
        applyRoutes(getServiceManager<IOperationService>())
    }
}
