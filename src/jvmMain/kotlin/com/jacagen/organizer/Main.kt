package com.jacagen.organizer

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.getAllServiceManagers
import io.kvision.remote.getServiceManager
import io.kvision.remote.kvisionInit


fun Application.main() {
    install(Compression)
    kvisionInit()
}
