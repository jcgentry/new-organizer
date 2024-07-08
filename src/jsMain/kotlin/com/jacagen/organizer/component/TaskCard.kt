package com.jacagen.organizer.component

import io.kvision.core.onEvent
import org.w3c.dom.svg.SVGElement

fun TaskBoard.taskCard(id: String, className: String, x: Int, y: Int, width: Int, height: Int, fill: Color): TaskCard {
    val card = TaskCard(id, className, x, y, width, height, fill)
    addCard(card)
    return card
}


class TaskCard(id: String, className: String, x: Int, y: Int, width: Int, height: Int, fill: Color) :
    Rect(id, className, x, y, width, height, fill) {

    init {
        onEvent {
            /* See https://www.petercollingridge.co.uk/tutorials/svg/interactive/dragging/ */
            mousedown = { e -> findTaskBoard().startDrag(self, e) }
            mousemove = { e -> findTaskBoard().drag(e) }
            mouseup = { e -> findTaskBoard().endDrag(e) }
        }
    }

    private fun findTaskBoard(): TaskBoard {
        var c = parent!!
        while (c !is TaskBoard)
            c = c.parent!!
        return c
    }

    fun getScreenCTM() = getElement().asDynamic().getScreenCTM()
}