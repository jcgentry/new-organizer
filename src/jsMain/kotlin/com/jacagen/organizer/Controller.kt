package com.jacagen.organizer

import com.jacagen.organizer.component.OutlineHelper
import com.jacagen.organizer.component.OutlineNode
import com.jacagen.organizer.component.TaskComponent
import kotlin.js.Console

private object Helper : OutlineHelper<Task, TaskComponent> {
    override fun createComponent(payload: Task) = TaskComponent(payload)

    override fun newNodeRequestor(parent: Node<Task>, position: Int?): Node<Task> {
        console.log("Adding new node to $parent at position $position")
        val newPayload = Task(newGuid(), "")
        val newNode = Node(newPayload, parent, tree = parent.tree)
        parent.children.add(if (position == null) parent.children.size else position, newNode)
        return newNode
    }

}

class Controller(console: Console) {
    val outline: OutlineNode<Task, TaskComponent>
    val tree: Tree<Task> = Tree { s -> console.log(s) }

    init {
        val rootOp = AddNode(parent = null, newGuid(), "\$ROOT")
        val newNode = rootOp.apply(tree) { s -> console.log(s) }
        outline = OutlineNode(newNode, Helper)
        emit(rootOp)
    }

    fun titleUpdated(task: Task, newTitle: String) {
        console.log("Title updated")
        val op = UpdateTitle(task.id, task.title, newTitle)
        console.log("Applying update title op")
        op.apply(tree) { s -> console.log(s) }
        console.log("Applied update title op")
        emit(op)
    }

    private fun <R> emit(@Suppress("UNUSED_PARAMETER") op: Operation<R>) {
        /* Nothing for now */
    }

    fun testTree(): Tree<Task> {
        val op1 = AddNode(tree.root!!.payload.id, newGuid(), title = "Entertain regularly")
        val n1 = op1.apply(tree) { s -> console.log(s) }
        outline.addChild(n1, 0)
        emit(op1)
        val op2 = AddNode(tree.root!!.payload.id, newGuid(), title = "Chris's birthday")
        val n2 = op2.apply(tree) { s -> console.log(s) }
        val cbOutlineNode = outline.addChild(n2, 1)
        emit(op2)
        val op3 = AddNode(n2.payload.id, newGuid(), title = "Figure out plans for Chris's birthday")
        val n3 = op3.apply(tree) { s -> console.log(s) }
        cbOutlineNode.addChild(n3, 0)
        emit(op3)
        return tree
    }
}