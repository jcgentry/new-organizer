package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import io.kvision.core.Container
import io.kvision.core.onClickLaunch
import io.kvision.core.onInputLaunch
import io.kvision.form.FormPanel
import io.kvision.form.formPanel
import io.kvision.form.text.Text
import io.kvision.form.text.text
import io.kvision.html.*
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.table.Row
import io.kvision.utils.auto
import io.kvision.utils.ch
import io.kvision.utils.px
import kotlin.text.Typography.nbsp


inline fun <reified T : Any> Container.findChild(): T = getChildren().first { it is T } as T

class Outline<T : Any>(root: T, label: (T) -> String, children: (T) -> List<T>) : Table() {
    var enabledRow: Text? = null

    init {
        apply {
            for ((indent, thisNode) in flattenNode(root, children)) {
                row(indent, thisNode, label)
            }
        }
    }

    private fun row(indent: Int, node: T, label: (T) -> String) {
        val row = OutlineRow(indent, node, label)
        add(row)
    }
}

class OutlineRow<T : Any>(val indent: Int, val node: T, val label: (T) -> String) : VPanel() {
    init {
        hPanel {
            spacer(indent)
            val panel = FormPanel<T>()
            panel.apply {
                val t = resizableText(label(node))
            }
            add(panel)
        }
        onClickLaunch {
            console.log(getChildren())
            console.log(findChild<HPanel>().getChildren())
            findChild<HPanel>().findChild<FormPanel<T>>().findChild<Text>().readonly = false
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