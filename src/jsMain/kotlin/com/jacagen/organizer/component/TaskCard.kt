package com.jacagen.organizer.component

import io.kvision.core.onEvent
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.svg.SVGGraphicsElement

class TaskCard(
    className: String = "draggable",
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fill: Color = "#888"
) : Rect(className, x, y, width, height, fill) {
    // This is not actually specifically for tasks...

    private var offset: Pair<Double, Double>? = null

    init {
        onEvent {
            mousedown = { e -> startDrag(e) }
            mousemove = { e -> drag(e) }
            mouseleave = { e ->
                console.log("Mouse leave $e")
            }
        }
    }

    private fun startDrag(e: Event) {
        val element = e.target as Element
        if (element != getElement()) throw IllegalStateException()
        if (element.classList.contains("draggable") && e is MouseEvent) {
            console.log("Draggable at ${element.getAttributeNS(null, "x")}")
            val (posX, posY) = getMousePosition(e)
            offset = Pair(
                posX - element.getAttributeNS(null, "x")!!.toDouble(),
                posY - element.getAttributeNS(null, "y")!!.toDouble(),
            )
        } else
            console.log("Not Draggable ${element.classList}")
    }

    private fun drag(e: Event) {
        if (e is MouseEvent) {
            e.preventDefault()
            val ctm = (e.target as SVGGraphicsElement).getScreenCTM()!!
            console.log("${ctm.a}, ${ctm.b}, ${ctm.e}, ${ctm.f}")
            val (x, y) = getMousePosition(e)
            val (ox, oy) = offset!!
            getElement()!!.setAttributeNS(null, "x", (x - ox).toString())
            getElement()!!.setAttributeNS(null, "y", (y - oy).toString())
        }
    }

    private fun getMousePosition(event: MouseEvent): Pair<Double, Double> {
        val ctm = (event.target as SVGGraphicsElement).getScreenCTM()!!
        console.log("${ctm.a}, ${ctm.b}, ${ctm.e}, ${ctm.f}")
        val x = (event.clientX - ctm.e) / ctm.a
        val y = (event.clientY - ctm.f) / ctm.d
        return Pair(x, y)
    }
}
