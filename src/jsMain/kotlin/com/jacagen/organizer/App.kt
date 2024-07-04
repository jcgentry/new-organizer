package com.jacagen.organizer

import com.jacagen.organizer.component.spacer
import io.kvision.*
import io.kvision.core.Component
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.panel.HPanel
import io.kvision.panel.Root
import io.kvision.panel.root
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {

    override fun start() {
        AppScope.launch {
            val controller = Controller.new(console)
            root("kvapp") {
                outlineView(controller)
            }
        }
    }

    private fun Root.outlineView(controller: Controller) {
        try {
            add(controller.outline)
        } catch (e: Exception) {
            console.log(e.message ?: "Something went wrong")
        }
        add(buttons(controller))
    }

    private fun buttons(controller: Controller): Component {
        return HPanel {
            button("Show outline").onClickLaunch {
                console.log(controller.tree.toString())
            }
            spacer(.1f)
            button("Add test tree").onClickLaunch { controller.testTree() }
            spacer(.1f)
            button("Show DB").onClickLaunch {
                extractOps()
            }
        }
    }


    private suspend fun extractOps() =
        AppScope.launch {
            Controller.allOps().forEach { console.log(it) }
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
