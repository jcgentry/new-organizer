package com.jacagen.organizer.view

import com.jacagen.organizer.component.*
import io.kvision.core.Container
import io.kvision.html.div

fun Container.taskBoardView() {
    div {
        val taskBoard = TaskBoard()
        add(taskBoard)
        taskBoard.taskCard(className = "draggable", x = 4, y = 5, width = 8, height = 10, fill = "#007bff")
        taskBoard.taskCard(className = "static", x = 18, y = 5, width = 8, height = 10, fill = "#888")
    }
}

