package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import io.kvision.core.Container
import io.kvision.form.FormPanel
import io.kvision.form.formPanel
import io.kvision.form.text.text
import io.kvision.html.Table
import io.kvision.html.div
import io.kvision.html.td
import io.kvision.html.tr
import io.kvision.table.Row
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
        tr {
            td {
                val panel = FormPanel<T>()
                panel.apply { div(nbsp.toString().repeat(indent * 10) + label(node)) }
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