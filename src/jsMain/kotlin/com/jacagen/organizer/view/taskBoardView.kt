package com.jacagen.organizer.view

import com.jacagen.organizer.component.*
import io.kvision.core.Container
import io.kvision.html.div

fun Container.taskBoardView() {
    div {
        val taskBoard = TaskBoard()
        add(taskBoard)
        val from = taskBoard.taskCard("one", className = "draggable", x = 4, y = 5, width = 8, height = 10, fill = "#007bff")
        val to = taskBoard.taskCard("two", className = "static", x = 18, y = 5, width = 8, height = 10, fill = "#888")
        taskBoard.addConnection(from, to)
    }
}

