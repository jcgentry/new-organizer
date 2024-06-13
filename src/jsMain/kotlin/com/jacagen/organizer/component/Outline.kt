package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import com.jacagen.organizer.Tree
import io.kvision.core.Container
import io.kvision.core.onEvent
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel

class Outline<T : Any>(
    tree: Tree<T>,
    private val containerFun: (T) -> Container,
) : VPanel() {
    init {
        for ((node, level) in tree.depthFirst()) {
            val row = OutlineRow(level, node, containerFun)
            row.onEvent() {   // TODO onEventLaunch?
                keydown = { e ->
                    if (e.key == "Enter") {
                        console.log(e)
                        e.preventDefault()
                        e.stopPropagation()
                        addRowAfter(row)
                    }
                }
            }
            add(row)
        }
    }

    fun addRowAfter(row: OutlineRow<T>) {
        val node = row.node
        val newNode = node.newNodeAfter()
        val newRow = OutlineRow(row.indent, newNode, containerFun)
        val myIndex = row.parent!!.getChildren().indexOf(row)

        for (i in (myIndex + 1)..<(row.parent!!.getChildren().size)) {
            if (((row.parent!!.getChildren()[i]) as OutlineRow<T>).indent <= row.indent) {
                row.parent!!.add(myIndex + 1, newRow)
                return
            }
            row.parent!!.add(row.parent!!.getChildren().size - 1, newRow)
        }
    }

}

fun <T : Any> Container.outline(tree: Tree<T>, containerFun: (T) -> Container) {
    val o = Outline(tree, containerFun)
    add(o)
}

class OutlineRow<T : Any>(
    val indent: Int,
    val node: Node<T>,
    containerFun: (T) -> Container,
): HPanel() {
    init {
        spacer(indent)
        val nodeContainer = containerFun(node.payload)
        add(nodeContainer)
    }
}