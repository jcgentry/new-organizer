package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.html.Div
import io.kvision.utils.px

class Spacer(size: Int) : Div() {
    init {
        width = (size * 50).px
    }
}

fun Container.spacer(size: Int) {
    val s = Spacer(size)
    add(s)
}