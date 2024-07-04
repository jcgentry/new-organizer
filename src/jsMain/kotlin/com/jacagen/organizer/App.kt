package com.jacagen.organizer

import com.jacagen.organizer.component.spacer
import io.kvision.*
import io.kvision.core.*
import io.kvision.dropdown.dropDown
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.panel.HPanel
import io.kvision.panel.Root
import io.kvision.panel.root
import io.kvision.panel.stackPanel
import io.kvision.utils.px
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
                dropDown(
                    tr("Activate panel from the stack"), listOf(
                        tr("Outline view") to "#/outlineView",
                        tr("Box view") to "#/boxView"
                    )
                )

                stackPanel {
                    route("/outlineView") {

                        outlineView(controller)
                    }
                    route("/boxView") {
                        div("&nbsp;", rich = true) {
                            background = Background(Color.name(Col.GREEN))
                            height = 40.px
                        }
                    }
                }
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
