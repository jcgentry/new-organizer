package com.jacagen.organizer

import com.jacagen.organizer.component.Outline
import com.jacagen.organizer.component.OutlineRow
import com.jacagen.organizer.component.TaskComponent
import kotlin.js.Console

class Controller(console: Console) {
    val outline: Outline<Task>
    val tree: Tree<Task> = Tree { s -> console.log(s) }

    init {
        val rootOp = AddNode(parent = null, newGuid(), "\$ROOT")
        rootOp.apply(tree) { s -> console.log(s) }
        outline = Outline(tree,
            containerFun = { t ->
                val c = TaskComponent(t)
                c.onTitleUpdateLaunch { title ->
                    console.log("Title for task ${ c.task.id } has been updated")
                    titleUpdated(t, title)
                }
                c
            },
            newPayload = {
                console.log("Created new task")
                val task = Task(newGuid(), "")
                console.log("Created new task $task")
                task

            })
        emit(rootOp)
        outline.onRowAdded { parentPayload, newPayload, index ->
            console.log("Adding row for $newPayload")
            addRow(
                parentPayload,
                newPayload,
                index
            )
        }
    }

    fun titleUpdated(task: Task, newTitle: String) {
        console.log("Title updated")
        val op = UpdateTitle(task.id, task.title, newTitle)
        console.log("Applying update title op")
        op.apply(tree) { s -> console.log(s) }
        console.log("Applied update title op")
        emit(op)
    }

    fun addRow(parent: Task, task: Task, index: Int?): Task {
        val parentNode = tree.get { t -> t == parent }
        val op = AddNode(
            parentNode.payload.id,
            task.id, task.title,
            if (index == null) parentNode.children.size else index)
        val newNode = op.apply(tree) { s -> console.log(s) }
        return newNode.payload
    }

    private fun <R> emit(op: Operation<R>) {
        /* Nothing for now */
    }

    fun testTree(): Tree<Task> {
        val op1 = AddNode(tree.root!!.payload.id, newGuid(), label = "Entertain regularly")
        val n1 = op1.apply(tree) { s -> console.log(s) }
        outline.addChild(outline.root, n1.payload)
        emit(op1)
        val op2 = AddNode(tree.root!!.payload.id, newGuid(), label = "Chris's birthday")
        val n2 = op2.apply(tree) { s -> console.log(s) }
        val o2 = outline.addChild(outline.root, n2.payload)
        emit(op2)
        val op3 = AddNode(n2.payload.id, newGuid(), label = "Figure out plans for Chris's birthday")
        val n3 = op3.apply(tree) { s -> console.log(s) }
        outline.addChild(o2, n3.payload)
        emit(op3)
        return tree
    }
}