package com.jacagen.organizer

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.kvision.remote.kvisionInit


@Suppress("unused")
fun Application.main() {
    install(Compression)
    kvisionInit()
}
