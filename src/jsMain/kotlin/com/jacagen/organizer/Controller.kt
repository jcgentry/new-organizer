package com.jacagen.organizer

import com.jacagen.organizer.component.OutlineHelper
import com.jacagen.organizer.component.OutlineNode
import com.jacagen.organizer.component.TaskComponent
import kotlinx.coroutines.launch
import kotlin.js.Console

private class Helper(private val controller: Controller) : OutlineHelper<Task, TaskComponent> {
    override fun createComponent(payload: Task): TaskComponent {
        val component = TaskComponent(payload)
        component.onTitleUpdateLaunch { s ->
            controller.titleUpdated(payload ,s)
        }
        return component
    }

    override fun newNodeRequestor(parent: Node<Task>, position: Int?): Node<Task> {
        console.log("Adding new node to $parent at position $position")
        val newPayload = Task(newGuid(), "")
        val newNode = Node(newPayload, parent, tree = parent.tree)
        parent.children.add(position ?: parent.children.size, newNode)
        return newNode
    }
}

class Controller(console: Console) {
    val outline: OutlineNode<Task, TaskComponent>
    val tree: Tree<Task> = Tree { s -> console.log(s) }

    init {
        val helper = Helper(this)
        val rootOp = AddNode(parent = null, node = newGuid(), title = "\$ROOT")
        val newNode = rootOp.apply(tree) { s -> console.log(s) }
        outline = OutlineNode(newNode, helper, isRoot = true)
        emit(rootOp)
    }

    fun titleUpdated(task: Task, newTitle: String) {
        console.log("Title updated")
        val op = UpdateTitle(node = task.id, oldTitle = task.title, title = newTitle)
        console.log("Applying update title op")
        op.apply(tree) { s -> console.log(s) }
        console.log("Applied update title op")
        emit(op)
    }

    suspend fun allOps(): List<Operation> = Model.allOps()

    private fun emit(op: Operation) {
        AppScope.launch {
            Model.saveOp(op)
        }
    }

    fun testTree(): Tree<Task> {
        val op1 = AddNode(parent = tree.root!!.payload.id, node = newGuid(), title = "Entertain regularly")
        val n1 = op1.apply(tree) { s -> console.log(s) }
        outline.addChild(n1, 0)
        emit(op1)
        val op2 = AddNode(parent = tree.root!!.payload.id, node = newGuid(), title = "Chris's birthday")
        val n2 = op2.apply(tree) { s -> console.log(s) }
        val cbOutlineNode = outline.addChild(n2, 1)
        emit(op2)
        val op3 = AddNode(parent = n2.payload.id, node = newGuid(), title = "Figure out plans for Chris's birthday")
        val n3 = op3.apply(tree) { s -> console.log(s) }
        cbOutlineNode.addChild(n3, 0)
        emit(op3)
        return tree
    }
}