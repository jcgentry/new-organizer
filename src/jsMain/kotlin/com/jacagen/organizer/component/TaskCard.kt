package com.jacagen.organizer.component

fun TaskBoard.taskCard(id: String, className: String, x: Int, y: Int, width: Int, height: Int, fill: Color): TaskCard {
    val card = TaskCard(id, className, x, y, width, height, fill)
    addCard(card)
    return card
}

class TaskCard(id: String, className: String, x: Int, y: Int, width: Int, height: Int, fill: Color) : Rect(id, className, x, y, width, height, fill) {}