package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.html.Div
import io.kvision.utils.px

class Spacer(size: Float) : Div() {
    init {
        width = (size * 50.0).toInt().px
    }
}

fun Container.spacer(size: Float) {
    val s = Spacer(size)
    add(s)
}

fun Container.spacer(size: Int) {
    val s = Spacer(size.toFloat())
    add(s)
}