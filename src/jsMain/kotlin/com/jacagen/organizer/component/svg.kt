package com.jacagen.organizer.component

import io.kvision.html.CustomTag
import io.kvision.html.Div
import io.kvision.panel.VPanel
import org.w3c.dom.svg.SVGGraphicsElement

typealias Color = String

data class ViewBox(
    val minX: Int,
    val minY: Int,
    val width: Int,
    val height: Int,
) {
    override fun toString() = "$minX $minY $width $height"
}

class Svg(width: Int? = null, height: Int? = null, viewBox: ViewBox? = null, block: Svg.() -> Unit = {}) :
    CustomTag(elementName = "svg") {
    init {
        if (width != null) setAttribute("width", "$width")
        if (height != null) setAttribute("height", "$height")
        if (viewBox != null) setAttribute("viewBox", "$viewBox")
        block()
    }
}

fun VPanel.svg(width: Int? = null, height: Int? = null, viewBox: ViewBox? = null, block: Svg.() -> Unit = {}): Svg {
    val svg = Svg(width, height, viewBox, block)
    add(svg)
    return svg
}

open class Rect(id: String, className: String? = null, x: Int, y: Int, width: Int, height: Int, fill: Color) : CustomTag("rect") {
    init {
        this.id = id
        if (className != null) addCssClass(className)
        setAttribute("x", "$x")
        setAttribute("y", "$y")
        setAttribute("width", "$width")
        setAttribute("height", "$height")
        setAttribute("fill", fill)
    }
}

fun Svg.rect(id: String, className: String? = null, x: Int, y: Int, width: Int, height: Int, fill: Color) {
    val rect = Rect(id, className, x, y, width, height, fill)
    add(rect)
}

class Line(x1: Int, y1: Int, x2: Int, y2: Int, style: Map<String, String>) : CustomTag("line") { // Use class instead of style
    init {
        setAttribute("x1", "$x1")
        setAttribute("y1", "$y1")
        setAttribute("x2", "$x2")
        setAttribute("y2", "$y2")
        for ((key, value) in style) {
            setStyle(key, value)
        }
    }
}

fun Svg.line(x1: Int, y1: Int, x2: Int, y2: Int, style: Map<String, String>) {
    val line = Line(x1, y1, x2, y2, style)
    add(line)
}