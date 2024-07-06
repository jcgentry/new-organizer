package com.jacagen.organizer.component

import io.kvision.html.Div
import io.kvision.require

class TaskBoard : Div() {
    init {
        require("css/taskboard.css")
        svg(viewBox = ViewBox(0, 0, 30, 20)) {
            rect(x = 0, y = 0, width = 30, height = 20, fill = "#fafafa")
        }
    }

    private fun getSVG() = getChildren()[0] as Svg

    fun addTaskCard(taskCard: TaskCard) = getSVG().add(taskCard)
}