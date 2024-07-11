package com.jacagen.organizer.view

import com.jacagen.organizer.Controller
import com.jacagen.organizer.component.*
import io.kvision.core.Container
import io.kvision.html.div

fun Container.taskBoardView(controller: Controller) {
    div {
        add(controller.taskBoard)
    }
}

