package com.jacagen.organizer.component

import com.jacagen.organizer.Tree
import io.kvision.core.Container
import io.kvision.core.onEvent
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import org.w3c.dom.events.Event


class Outline<T : Any>(
    tree: Tree<T>,
    private val containerFun: (T) -> Container,
    private val newPayload: () -> T,
) : VPanel() {
    val root: OutlineRow<T>
    private var addRowHandler: ((T, T, Int?) -> Unit)? = null

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

    fun addChild(parent: OutlineRow<T>, child: T): OutlineRow<T> {
        val newRow = OutlineRow(parent.indent + 1, child, containerFun)
        parent.parent!!.add(newRow)
        if (addRowHandler != null)
            addEnterHandler(newRow)
        return newRow
    }

    private fun addEnterHandler(row: OutlineRow<T>) {
        row.onEvent {   // TODO onEventLaunch?
            keydown = { e ->
                if (e.key == "Enter") {
                    enterHandler(row, e)
                }
            }
        }
    }

    private fun enterHandler(row: OutlineRow<T>, e: Event) {
        e.preventDefault()
        e.stopPropagation()
        val newPayload = newPayload()
        if (row == root) {
            val newRow = addChild(root, newPayload())
            addRowHandler!!(root.payload, newRow.payload, null)
            newRow.focus()
        } else {
            val newRow = OutlineRow(row.indent, newPayload, containerFun)
            addRowAfter(row, newRow)
            val (idx, parent: OutlineRow<T>?) = locateRow(newRow)
            addRowHandler!!(parent!!.payload, newRow.payload, idx)
            newRow.focus()
        }
    }

    /** Figure out index of row relative to its conceptual parent */
    private fun locateRow(newRow: OutlineRow<T>): Pair<Int, OutlineRow<T>?> {
        var idx = 0
        var parent: OutlineRow<T>? = null
        for (c in newRow.parent!!.getChildren()) {
            val childRow = c.unsafeCast<OutlineRow<T>>()
            if (childRow == newRow)
                break
            else if (childRow.indent == newRow.indent - 1)
                parent = childRow
            else if (childRow.indent == newRow.indent)
                idx++
        }
        return Pair(idx, parent)
    }

    private fun addRowAfter(row: OutlineRow<T>, newRow: OutlineRow<T>) {
        val myIndex = row.parent!!.getChildren().indexOf(row)
        for (i in (myIndex + 1)..<(row.parent!!.getChildren().size)) {
            if (((row.parent!!.getChildren()[i]) as OutlineRow<*>).indent <= row.indent) {
                row.parent!!.add(myIndex + 1, newRow)
                if (addRowHandler != null)
                    addEnterHandler(newRow)
                newRow.focus()
                return
            }
        }
        row.parent!!.add(row.parent!!.getChildren().size, newRow)
        if (addRowHandler != null)
            addEnterHandler(newRow)
        newRow.focus()
    }
}

class OutlineRow<T : Any>(
    val indent: Int,
    val payload: T,
    containerFun: (T) -> Container,
) : HPanel() {
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