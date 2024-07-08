package com.jacagen.organizer.component

import io.kvision.core.Component
import io.kvision.html.Div
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.require
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.math.abs

class TaskBoard : Div() {
    private var selectedElement: TaskCard? = null
    private var offset: Pair<Double, Double>? = null
    private val connections: MutableMap<Pair<TaskCard, TaskCard>, Line> = mutableMapOf()

    var svg: Svg? = null

    private var mouseXReporter = Div("Mouse x:")
    private val mouseYReporter = Div("Mouse y:")
    private val clientXReporter = Div("Client x:")
    private val clientYReporter = Div("Client y:")

    init {
        require("css/taskboard.css")
        vPanel {
            svg = svg(viewBox = ViewBox(0, 0, 30, 20)) {
                rect(id = "zero", x = 0, y = 0, width = 30, height = 20, fill = "#fafafa")
            }
            hPanel {
                add(mouseXReporter)
                add(mouseYReporter)
            }
            hPanel {
                add(clientXReporter)
                add(clientYReporter)
            }
        }
    }

    fun getSvg(): Svg {
        console.log(getChildren()[0])
        val vPanel = getChildren()[0] as VPanel
        val svg = vPanel.getChildren()[0] as Svg
        return svg
        //return (getChildren()[0] as VPanel).getChildren()[0] as Svg
    }


    fun startDrag(component: Component, e: Event) {
        if (component.getElement()!!.classList.contains("draggable") && e is MouseEvent) {
            selectedElement = component as TaskCard
            val (posX, posY) = getMousePosition(component, e)
            offset = Pair(
                posX - selectedElement!!.getAttribute("x")!!.toDouble(),
                posY - selectedElement!!.getAttribute("y")!!.toDouble(),)
        } else {
            console.log("Not draggable")
        }
    }

    fun drag(e: Event) {
        if (e is MouseEvent && selectedElement != null) {
            e.preventDefault()
            val (x, y) = getMousePosition(selectedElement!!, e)
            val (ox, oy) = offset!!
            val posX = (x - ox)
            val posY = (y - oy)
            selectedElement!!.setAttribute("x", posX.toString())
            selectedElement!!.setAttribute("y", posY.toString())
            console.log("Changed card to $posX, $posY")
            for ((cards, line) in connections.entries) {
                if (cards.first == selectedElement || cards.second == selectedElement) {
                    console.log("Found element")
                    console.log("Selected element is ${cards.first.getElement()}")
                    updateConnection(cards.first, cards.second, line)
                }
            }
        }
    }

    private fun getMousePosition(component: TaskCard, event: MouseEvent): Pair<Double, Double> {
        console.log("Mouse position of component $component, with element ${component.getElement()}")
        val ctm = component.getScreenCTM()!!
        val e = ctm.e as Int
        val f = ctm.f as Int
        val a = ctm.a as Int
        val d = ctm.d as Int
        clientXReporter.content =
            "Client x: ${event.clientX}"
        clientYReporter.content =
            "Client y: ${event.clientY}"
        val x = (event.clientX - e).toDouble() / a.toDouble()
        val y = (event.clientY - f).toDouble() / d.toDouble()
        mouseXReporter.content = "Mouse x: $x"
        mouseYReporter.content = "Mouse y: $y"
        return Pair(x, y)
    }

    @Suppress("UNUSED_PARAMETER")
    fun endDrag(e: Event) {
        selectedElement = null
    }

    fun addCard(card: TaskCard) {
        getSvg().add(card)

    }

    fun addConnection(from: TaskCard, to: TaskCard) {
        val line = Line(0, 0, 100, 100, mapOf("stroke" to "red", "stroke-width" to "0.1"))
        connections[Pair(from, to)] = line
        console.log("Add connection from $from to $to")
        //from.getElement()
        getSvg().add(line)
        updateConnection(from, to, line)
    }

    fun updateConnection(from: TaskCard, to: TaskCard, cxn: Line) {
        /* See https://stackoverflow.com/questions/50252070/svg-draw-connection-line-between-two-rectangles */

        /* Top left coordinates */
        val x1 = from.getAttribute("x")!!.toFloat()
        val y1 = from.getAttribute("y")!!.toFloat()
        val x2 = to.getAttribute("x")!!.toFloat()
        val y2 = to.getAttribute("y")!!.toFloat()
        console.log("Top-left coordinates are ($x1, $y1), ($x2, $y2)")

        /* Half widths and half heights */
        val w1 = from.getAttribute("width")!!.toFloat() / 2.0f
        val h1 = from.getAttribute("height")!!.toFloat() / 2.0f
        val w2 = to.getAttribute("width")!!.toFloat() / 2.0f
        val h2 = to.getAttribute("height")!!.toFloat() / 2.0f
        console.log("Sizes are $w1 x $h1, $w2, $h2")

        /* Center coordinates */
        val cx1 = x1 + w1
        val cy1 = y1 + h1
        val cx2 = x2 + w2
        val cy2 = y2 + h2
        console.log("Center coordinates are are $cx1, $cy1 -> $cx2, $cy2")

        /* Distance between centers */
        val dx = cx2 - cx1
        val dy = cy2 - cy1

        val p1 = getIntersection(dx, dy, cx1, cy1, w1, h1)
        val p2 = getIntersection(-dx, -dy, cx2, cy2, w2, h2)

        cxn.setAttribute("x1", p1.first.toString())
        cxn.setAttribute("y1", p1.second.toString())
        cxn.setAttribute("x2", p2.first.toString())
        cxn.setAttribute("y2", p2.second.toString())
        console.log("Connection is now $x1, $y1 -> $x2, $y2")
    }

    fun getIntersection(dx: Float, dy: Float, cx: Float, cy: Float, w: Float, h: Float): Pair<Float, Float> {
        if (abs(dy / dx) < h / w) {
            /*  Hit vertical edge of box1 */
            return Pair(cx + (if (dx > 0) w else -w), cy + dy * w / abs(dx))
        } else {
            // Hit horizontal edge of box1
            return Pair(cx + dx * h / abs(dy), cy + (if (dy > 0) h else -h))
        }
    }
}

