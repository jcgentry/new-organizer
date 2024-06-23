package com.jacagen.organizer

import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.panel.root

class App : Application() {
    val controller = Controller(console)
//    val tree = testTree()

    override fun start() {
        println("Starting application")
        root("kvapp") {
            try {
                console.log("Adding outline")
                controller.outline.root.nodeContainer.addAfterInsertHook { vnode ->
                    console.log("After insert hook")
                }
                add(controller.outline)
                console.log("Added outline")
            } catch (e: Exception) {
                console.log(e.message ?: "Something went wrong")
            }
            button("Show outline").onClickLaunch {
                console.log(controller.tree.toString())
            }
            button("Add test tree").onClickLaunch { controller.testTree() }
        }
        console.log("Set up root")
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
