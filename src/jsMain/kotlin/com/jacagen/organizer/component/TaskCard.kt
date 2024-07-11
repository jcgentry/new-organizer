package com.jacagen.organizer.component

import io.kvision.core.onEvent
import org.w3c.dom.svg.SVGElement

fun TaskBoard.taskCard(
    content: String,
    className: String,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fill: Color,
): TaskCard {
    val card = TaskCard(content, className, x, y, width, height, fill)
    addCard(card)
    return card
}

private const val X_TEXT_DELTA = 1
private const val Y_TEXT_DELTA = 1

class TaskCard(
    content: String,
    className: String,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fill: Color,
) : G() {
    private val rect = Rect(className, x, y, width, height, fill)
    private val text: Text

    init {
        io.kvision.require("css/task-board.css")
        text = Text(className = "taskCardTitle", x + X_TEXT_DELTA, y + Y_TEXT_DELTA, content)
        add(rect)
        add(text)
        rect.onEvent {
            /* See https://www.petercollingridge.co.uk/tutorials/svg/interactive/dragging/ */
            mousedown = { e -> findTaskBoard().startDrag(this@TaskCard, e) }
            mousemove = { e -> findTaskBoard().drag(e) }
            mouseup = { e -> findTaskBoard().endDrag(e) }
        }
    }

    override fun setAttribute(name: String, value: String) {
        if (name == "x") {
            val intValue = value.toDouble()
            rect.setAttribute(name, value)
            text.setAttribute(name, (intValue + X_TEXT_DELTA).toString())
        } else if (name == "y") {
            val intValue = value.toDouble()
            rect.setAttribute(name, value)
            text.setAttribute(name, (intValue + Y_TEXT_DELTA).toString())
        } else super.setAttribute(name, value)
    }

    override fun getAttribute(name: String): String? =
        if (name == "x" || name == "y" || name == "height" || name == "width")
            rect.getAttribute(name)
        else super.getAttribute(name)

    private fun findTaskBoard(): TaskBoard {
        var c = parent!!
        while (c !is TaskBoard)
            c = c.parent!!
        return c
    }

    fun getScreenCTM() = getElement().asDynamic().getScreenCTM()
}