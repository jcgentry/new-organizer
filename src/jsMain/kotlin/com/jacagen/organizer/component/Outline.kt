package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import io.kvision.core.Container
import io.kvision.form.FormPanel
import io.kvision.form.formPanel
import io.kvision.form.text.text
import io.kvision.html.*
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.table.Row
import io.kvision.utils.px
import kotlin.text.Typography.nbsp

class Outline<T : Any>(root: T, label: (T) -> String, children: (T) -> List<T>) : Table() {
    init {
        apply {
            for ((indent, thisNode) in flattenNode(root, children)) {
                row(indent, thisNode, label)
            }
        }
    }

    private fun row(indent: Int, node: T, label: (T) -> String) {
        vPanel {
            hPanel {
                val div = Div()
                div.width = (indent * 50).px
                add(div)
                val panel = FormPanel<T>()
                panel.apply { div(label(node)) }
                add(panel)
            }
        }
    }
}

private fun <T> flattenNode(node: T, children: (T) -> List<T>, level: Int = 0): Sequence<Pair<Int, T>> = sequence {
    yield(Pair(level, node))
    for (c in children(node)) {
        yieldAll(flattenNode(c, children, level + 1))     // Too much recursion?
    }
}

fun <T : Any> Container.outline(root: T, label: (T) -> String, children: (T) -> List<T>) {
    div("outline")
    val o = Outline(root, label, children)
    add(o)
}