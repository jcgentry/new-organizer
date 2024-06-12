package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import com.jacagen.organizer.Tree
import io.kvision.core.Container
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel

class Outline<T : Any>(
    tree: Tree<T>,
    containerFun: (T) -> Container,
) : VPanel() {
    init {
        for ((node, level) in tree.depthFirst()) {
            val row = OutlineRow(level, node, containerFun)
            add(row)
        }
    }
}

fun <T : Any> Container.outline(tree: Tree<T>, containerFun: ((T) -> Container)) {
    val o = Outline(tree, containerFun)
    add(o)
}

class OutlineRow<T>(
    indent: Int,
    node: Node<T>,
    containerFun: (T) -> Container,
): HPanel() {
    init {
        spacer(indent)
        val nodeContainer = containerFun(node.payload)
        add(nodeContainer)
    }

}