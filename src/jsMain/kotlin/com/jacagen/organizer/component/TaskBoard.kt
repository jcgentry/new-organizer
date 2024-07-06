package com.jacagen.organizer.component

import io.kvision.html.Div
import io.kvision.require

class TaskBoard : Div() {
    init {
        require("css/taskboard.css")
        svg(viewBox = ViewBox(0, 0, 30, 20)) {
            rect(x = 0, y = 0, width = 30, height = 20, fill = "#fafafa")

            add(TaskCard(className = "draggable", x = 4, y = 5, width = 8, height = 10, fill = "#007bff"))
            add(TaskCard(className = "static", x = 18, y = 5, width = 8, height = 10, fill = "#888"))
        }
    }
}