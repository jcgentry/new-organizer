package com.jacagen.organizer

import com.jacagen.organizer.view.outlineView
import com.jacagen.organizer.view.taskBoardView
import io.kvision.*
import io.kvision.core.*
import io.kvision.dropdown.dropDown
import io.kvision.i18n.I18n.tr
import io.kvision.panel.*
import io.kvision.routing.Routing
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {
    init {
        Routing.init()
    }

    override fun start() {
        AppScope.launch {
            val controller = Controller.new(console)
            root("kvapp") {
                this.marginTop = 10.px
                vPanel(spacing = 5) {
                    addStackPanel(controller)
                }
            }
        }
    }

    private fun Container.addStackPanel(controller: Controller) {
        dropDown(
            tr("View"), listOf(
                tr("Outline") to "#/view/outline",
                tr("Boxes") to "#/view/boxes"
            )
        )
        stackPanel(activateLast = false) {
            route("/view/outline") {
                outlineView(controller)
            }
            route("/view/boxes") {
                taskBoardView(controller)
            }
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

