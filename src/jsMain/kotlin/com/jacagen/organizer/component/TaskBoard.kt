package com.jacagen.organizer.component

import io.kvision.core.onEvent
import io.kvision.html.Div
import io.kvision.require
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.svg.SVGGraphicsElement

class TaskBoard : Div() {
    private var selectedElement: Element? = null
    private var offset: Pair<Double, Double>? = null

    init {
        require("css/taskboard.css")
        val svg = svg(viewBox = ViewBox(0, 0, 30, 20)) {
            rect(x = 0, y = 0, width = 30, height = 20, fill = "#fafafa")
            rect(className = "draggable", x = 4, y = 5, width = 8, height = 10, fill = "#007bff")
            rect(className = "static", x = 18, y = 5, width = 8, height = 10, fill = "#888")
        }
        svg.onEvent {
            mousedown = { e -> startDrag(e) }
            mousemove = { e -> drag(e) }
            mouseup = { e -> endDrag(e) }
            mouseleave = { e ->
                console.log("Mouse leave $e")
            }
        }
    }

    private fun startDrag(e: Event) {
        val element = e.target as Element
        if (element.classList.contains("draggable") && e is MouseEvent) {
            console.log("Draggable at ${element.getAttributeNS(null, "x")}")
            selectedElement = element
            val (posX, posY) = getMousePosition(e)
            offset = Pair(
                posX - selectedElement!!.getAttributeNS(null, "x")!!.toDouble(),
                posY - selectedElement!!.getAttributeNS(null, "y")!!.toDouble(),
            )
        } else
            console.log("Not Draggable ${element.classList}")
    }

    private fun drag(e: Event) {
        if (e is MouseEvent && selectedElement != null) {
            e.preventDefault()
            val ctm = (e.target as SVGGraphicsElement).getScreenCTM()!!
            console.log("${ctm.a}, ${ctm.b}, ${ctm.e}, ${ctm.f}")
            val (x, y) = getMousePosition(e)
            val (ox, oy) = offset!!
            selectedElement!!.setAttributeNS(null, "x", (x - ox).toString())
            selectedElement!!.setAttributeNS(null, "y", (y - oy).toString())
        }
    }

    private fun getMousePosition(event: MouseEvent): Pair<Double, Double> {
        val ctm = (event.target as SVGGraphicsElement).getScreenCTM()!!
        console.log("${ctm.a}, ${ctm.b}, ${ctm.e}, ${ctm.f}")
        val x = (event.clientX - ctm.e) / ctm.a
        val y = (event.clientY - ctm.f) / ctm.d
        return Pair(x, y)

    }

    @Suppress("UNUSED_PARAMETER")
    private fun endDrag(e: Event) {
        selectedElement = null
    }
}