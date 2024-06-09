package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.html.Table
import io.kvision.html.div
import io.kvision.html.td
import io.kvision.html.tr
import kotlin.text.Typography.nbsp

class Outline(f: (Outline.() -> Unit)?) : Table() {
    init {
        f?.invoke(this)
    }

    fun row(indent: Int, label: String) {
        tr {
            td { div(nbsp.toString().repeat(indent * 10) + label) }
        }
    }
}

fun Container.outline(f: Outline.() -> Unit) {
    val o = Outline(f)
    add(o)
}