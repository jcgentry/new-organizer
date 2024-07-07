package com.jacagen.organizer.view

import com.jacagen.organizer.AppScope
import com.jacagen.organizer.Controller
import com.jacagen.organizer.component.spacer
import io.kvision.core.Component
import io.kvision.core.Container
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.panel.HPanel
import kotlinx.coroutines.launch

fun Container.outlineView(controller: Controller) {
    div {
        try {
            add(controller.outline)
        } catch (e: Exception) {
            console.log(e.message ?: "Something went wrong")
        }
        add(buttons(controller))
    }
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