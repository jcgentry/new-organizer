package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.html.Div
import io.kvision.utils.px

// TODO Can we make supertype more general?
class Spacer(private val size: Int) : Div() {
    init {
        width = (size * 50).px
    }
}

fun Container.spacer(size: Int) {
    val s = Spacer(size)
    add(s)
}