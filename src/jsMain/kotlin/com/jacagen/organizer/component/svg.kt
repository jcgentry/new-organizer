package com.jacagen.organizer.component

import io.kvision.html.CustomTag
import io.kvision.html.Div

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

fun Div.svg(width: Int? = null, height: Int? = null, viewBox: ViewBox? = null, block: Svg.() -> Unit = {}): Svg {
    val svg = Svg(width, height, viewBox, block)
    add(svg)
    return svg
}

open class Rect(className: String? = null, x: Int, y: Int, width: Int, height: Int, fill: Color) : CustomTag("rect") {
    init {
        if (className != null) addCssClass(className)
        setAttribute("x", "$x")
        setAttribute("y", "$y")
        setAttribute("width", "$width")
        setAttribute("height", "$height")
        setAttribute("fill", fill)
    }
}

fun Svg.rect(className: String? = null, x: Int, y: Int, width: Int, height: Int, fill: Color) {
    add(Rect(className, x, y, width, height, fill))
}