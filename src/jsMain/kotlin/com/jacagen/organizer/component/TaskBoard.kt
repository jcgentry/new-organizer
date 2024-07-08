package com.jacagen.organizer.component

import io.kvision.core.onEvent
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.require
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.svg.SVGGraphicsElement
import kotlin.math.abs

class TaskBoard : Div() {
    private var selectedElement: Element? = null
    private var offset: Pair<Double, Double>? = null
    private val connections: MutableMap<Pair<TaskCard, TaskCard>, Line> = mutableMapOf()

    init {
        require("css/taskboard.css")
        vPanel {
            val svg = svg(viewBox = ViewBox(0, 0, 30, 20)) {
                rect(id = "zero", x = 0, y = 0, width = 30, height = 20, fill = "#fafafa")
            }
            svg.onEvent {
                /* See https://www.petercollingridge.co.uk/tutorials/svg/interactive/dragging/ */
                mousedown = { e -> startDrag(e) }
                mousemove = { e -> drag(e) }
                mouseup = { e -> endDrag(e) }
            }
            hPanel {
                div("Mouse x:")
                div("Mouse y:")
            }
            hPanel {
                div("Client x:")
                div("Client y:")
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


    private fun startDrag(e: Event) {
        val element = e.target as Element
        if (element.classList.contains("draggable") && e is MouseEvent) {
            selectedElement = element
            val (posX, posY) = getMousePosition(e)
            offset = Pair(
                posX - selectedElement!!.getAttributeNS(null, "x")!!.toDouble(),
                posY - selectedElement!!.getAttributeNS(null, "y")!!.toDouble(),
            )
        }
    }

    private fun drag(e: Event) {
        if (e is MouseEvent && selectedElement != null) {
            e.preventDefault()
            val (x, y) = getMousePosition(e)
            val (ox, oy) = offset!!
            val posX = (x - ox).toString()
            val posY = (y - oy).toString()
            selectedElement!!.setAttribute("x", posX)
            selectedElement!!.setAttribute("y", posY)
            console.log("Changed card to $posX, $posY")
            for ((cards, line) in connections.entries) {
                val first = cards.first.id
                val second = cards.second.id
                if (first == selectedElement!!.id || second == selectedElement!!.id) {
                    console.log("Found element")
                    console.log("Selected element is ${cards.first.getElement()}")
                    console.log("Does ==?  ${cards.first.getElement() == selectedElement}")
                    updateConnection(cards.first, cards.second, line)
                }
            }
        }
    }

    private fun getMousePosition(event: MouseEvent): Pair<Double, Double> {
        val ctm = (event.target as SVGGraphicsElement).getScreenCTM()!!
        ((((getChildren()[0] as VPanel).getChildren()[1] as HPanel)).getChildren()[0] as Div).content = "Client x: ${event.clientX}"
        ((((getChildren()[0] as VPanel).getChildren()[2] as HPanel)).getChildren()[0] as Div).content = "Client y: ${event.clientY}"
        val x = (event.clientX - ctm.e) / ctm.a
        val y = (event.clientY - ctm.f) / ctm.d
        ((((getChildren()[0] as VPanel).getChildren()[1] as HPanel)).getChildren()[1] as Div).content = "Mouse x: $x"
        ((((getChildren()[0] as VPanel).getChildren()[2] as HPanel)).getChildren()[1] as Div).content = "Mouse y: $y"
        return Pair(x, y)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun endDrag(e: Event) {
        selectedElement = null
    }

    fun addCard(card: TaskCard) = getSvg().add(card)

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
        val cx1 = x1 + w1;
        val cy1 = y1 + h1;
        val cx2 = x2 + w2;
        val cy2 = y2 + h2;
        console.log("Center coordinates are are $cx1, $cy1 -> $cx2, $cy2")

        /* Distance between centers */
        val dx = cx2 - cx1;
        val dy = cy2 - cy1;

        val p1 = getIntersection(dx, dy, cx1, cy1, w1, h1);
        val p2 = getIntersection(-dx, -dy, cx2, cy2, w2, h2);

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

