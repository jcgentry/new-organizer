package com.jacagen.organizer

import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.panel.root
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {
    val controller = Controller(console)

    override fun start() {
        root("kvapp") {
            try {
                add(controller.outline)
            } catch (e: Exception) {
                console.log(e.message ?: "Something went wrong")
            }
            button("Show outline").onClickLaunch {
                console.log(controller.tree.toString())
            }
            button("Add test tree").onClickLaunch { controller.testTree() }
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule,
        FontAwesomeModule,
        TomSelectModule,
    )
}
