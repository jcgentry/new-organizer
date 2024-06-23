package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import com.jacagen.organizer.Tree
import io.kvision.core.Container
import io.kvision.core.Outline
import io.kvision.core.onEvent
import io.kvision.html.i
import io.kvision.html.p
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import org.w3c.dom.ChildNode


class Outline<T : Any>(
    tree: Tree<T>,
    private val containerFun: (T) -> Container,
    private val newPayload: () -> T,
) : VPanel() {
    val root: OutlineRow<T>
    var addRowHandler: ((T, T, Int?) -> Unit)? = null

    init {
        var root: OutlineRow<T>? = null
        for ((node, level) in tree.depthFirst()) {
            val row = OutlineRow(level, node.payload, containerFun)
            if (level == 0)
                root = row
            add(row)
            addEnterHandler(row)
        }
        this.root = root!!
    }

    fun onRowAdded(f: (T, T, Int?) -> Unit) {
        // TODO Need to handle existing rows
        addRowHandler = f
    }

    fun addChild(parent: OutlineRow<T>, child: T): OutlineRow<T>  {
        val newRow = OutlineRow(parent.indent + 1, child, containerFun)
        parent.parent!!.add(newRow)
        if (addRowHandler != null)
            addEnterHandler(newRow)
        return newRow
    }

    private fun addEnterHandler(row: OutlineRow<T>) {
        console.log("Adding enter handler to ${row.payload}")
        row.onEvent() {   // TODO onEventLaunch?
            console.log("OutlineRow onEvent on ${row.payload}")
            keydown = { e ->
                console.log("Keydown event on ${row.payload}")
                if (e.key == "Enter") {
                    console.log(e)
                    e.preventDefault()
                    e.stopPropagation()
                    val newPayload = newPayload()
                    if (row == root) {
                        val newRow = addChild(root, newPayload())
                        addRowHandler!!(root.payload, newRow.payload, null)
                        newRow.focus()
                    } else {
                        val newRow = OutlineRow<T>(row.indent, newPayload, containerFun)
                        addRowAfter(row, newRow)

                        /* Figure out index of row relative to its conceptual parent */
                        var idx = 0
                        var parent: OutlineRow<T>? = null
                        for (c in newRow.parent!!.getChildren()) {
                            val childRow = c as OutlineRow<T>
                            if (childRow == newRow)
                                break
                            else if (childRow.indent == newRow.indent - 1)
                                parent = childRow
                            else if (childRow.indent == newRow.indent)
                                idx++
                        }
                        addRowHandler!!(parent!!.payload, newRow.payload, idx)
                        newRow.focus()
                    }
                }
            }
        }
    }

    private fun addRowAfter(row: OutlineRow<T>, newRow: OutlineRow<T>) {
        val myIndex = row.parent!!.getChildren().indexOf(row)

        for (i in (myIndex + 1)..<(row.parent!!.getChildren().size)) {
            if (((row.parent!!.getChildren()[i]) as OutlineRow<T>).indent <= row.indent) {
                row.parent!!.add(myIndex + 1, newRow)
                if (addRowHandler != null)
                    addEnterHandler(newRow)
                console.log("About to focus")
                newRow.focus()
                return
            }
        }
        row.parent!!.add(row.parent!!.getChildren().size, newRow)
        if (addRowHandler != null)
            addEnterHandler(newRow)
        console.log("About to focus >")
        newRow.focus()
    }

}
//
//fun <T : Any> Container.outline(tree: Tree<T>, containerFun: (T) -> Container): Outline<T>{
//    val o = Outline(tree, containerFun)
//    add(o)
//    return o
//}

class OutlineRow<T : Any>(
    val indent: Int,
    val payload: T,
    containerFun: (T) -> Container,
): HPanel() {
    val nodeContainer: Container

    init {
        spacer(indent)
        nodeContainer = containerFun(payload)
        add(nodeContainer)
   }

    override fun focus() {
        nodeContainer.focus()
        super.focus()
    }
}