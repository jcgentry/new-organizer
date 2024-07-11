package com.jacagen.organizer.component

import io.kvision.html.CustomTag
import io.kvision.panel.VPanel

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

open class Rect(
    className: String? = null,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fill: Color,
    init: (CustomTag.() -> Unit)? = null
) : CustomTag("rect", init = init) {
    init {
        if (className != null) addCssClass(className)
        setAttribute("x", "$x")
        setAttribute("y", "$y")
        setAttribute("width", "$width")
        setAttribute("height", "$height")
        setAttribute("fill", fill)
        if (init != null) init()
    }
}

fun Svg.rect(
    className: String? = null,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fill: Color,
    init: (CustomTag.() -> Unit)? = null
) {
    val rect = Rect(className, x, y, width, height, fill)
    add(rect)
    if (init != null) init()
}

class Line(x1: Int, y1: Int, x2: Int, y2: Int, style: Map<String, String>) :
    CustomTag("line") { // Use class instead of style
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

class Text(className: String? = null, x: Int, y: Int, content: String) : CustomTag(
    "text", className = className, content = content
) {
    init {
        setAttribute("x", "$x")
        setAttribute("y", "$y")
    }
}

open class G : CustomTag("g")