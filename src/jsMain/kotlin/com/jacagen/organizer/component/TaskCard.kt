package com.jacagen.organizer.component

fun TaskBoard.taskCard(className: String, x: Int, y: Int, width: Int, height: Int, fill: Color) {
    getSvg().add(TaskCard(className, x, y, width, height, fill))
}

class TaskCard(className: String, x: Int, y: Int, width: Int, height: Int, fill: Color) : Rect(className, x, y, width, height, fill) {}