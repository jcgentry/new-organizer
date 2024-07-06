package com.jacagen.organizer.view

import com.jacagen.organizer.Controller
import com.jacagen.organizer.component.TaskBoard
import com.jacagen.organizer.component.TaskCard
import io.kvision.html.div
import io.kvision.panel.SimplePanel

fun SimplePanel.createTaskBoardView(@Suppress("UNUSED_PARAMETER") controller: Controller) {
    div {
        val taskBoard = TaskBoard()
        add(taskBoard)
        taskBoard.addTaskCard(TaskCard(className = "draggable", x = 4, y = 5, width = 8, height = 10, fill = "#007bff"))
        taskBoard.addTaskCard(TaskCard(className = "static", x = 18, y = 5, width = 8, height = 10, fill = "#888"))
    }
}