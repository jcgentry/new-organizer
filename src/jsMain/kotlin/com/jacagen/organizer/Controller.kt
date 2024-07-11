package com.jacagen.organizer

import com.jacagen.organizer.component.*
import io.kvision.html.div
import kotlinx.atomicfu.Trace
import kotlinx.coroutines.launch
import kotlin.js.Console

class Helper(private val controller: Controller) : OutlineHelper<Task, TaskComponent> {
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

class Controller private constructor (ops: List<Operation>, console: Console) {

    companion object {
        suspend fun new(console: Console): Controller {
            val ops = allOps()
            return Controller(ops, console)
        }

        suspend fun allOps(): List<Operation> = Model.allOps()
    }

    val tree: Tree<Task> = Tree { s -> console.log(s) }


    val outline: OutlineNode<Task, TaskComponent>
    private val helper = Helper(this)
    val taskBoard: TaskBoard = TaskBoard()

    init {
        if (ops.isEmpty()) {
            val rootOp = AddNode(parent = null, node = newGuid(), title = "\$ROOT")
            emit(rootOp)
            rootOp.applyOp(tree) { s -> console.log(s) }
        } else {
            ops.forEach { it.applyOp(tree) { s -> console.log(s)} }
        }
        outline = createNodesRecursive(tree.root!!, helper, isRoot = true)
        createTaskBoardRecursive(tree.root!!)
    }

    private fun createNodesRecursive(node: Node<Task>, helper: Helper, isRoot: Boolean): OutlineNode<Task,TaskComponent> {
        val outlineNode = OutlineNode(node, helper, isRoot)
        for (child in node.children)
            outlineNode.myChildList().add(createNodesRecursive(child, helper, false))
        return outlineNode
    }

    private fun createTaskBoardRecursive(node: Node<Task>, xOffset: Int = 0, yOffset: Int = 0, parent: TaskCard? = null): TaskBoard {
        val card = TaskCard(content = node.payload.title, className = "taskCard", x = xOffset * 3 + 5, -yOffset * 3 + 15, fill = "#888")
        taskBoard.addCard(card)
        if (parent != null)
            taskBoard.addConnection(parent, card)
        var x = 0
        for (child in node.children) {
            createTaskBoardRecursive(child, xOffset + x, yOffset + 2, card)
            x += 3
        }
        return taskBoard
    }

    fun titleUpdated(task: Task, newTitle: String) {
        console.log("Title updated")
        val op = UpdateTitle(node = task.id, oldTitle = task.title, title = newTitle)
        console.log("Applying update title op")
        op.applyOp(tree) { s -> console.log(s) }
        console.log("Applied update title op")
        emit(op)
    }

    private fun emit(vararg ops: Operation) {
        AppScope.launch {
            for (op in ops)
                Model.saveOp(op)
        }
    }

    fun testTree(): Tree<Task> {
        val op1 = AddNode(parent = tree.root!!.payload.id, node = newGuid(), title = "Entertain regularly")
        val n1 = op1.applyOp(tree) { s -> console.log(s) }
        outline.addChild(n1, 0, helper)
        val op2 = AddNode(parent = tree.root!!.payload.id, node = newGuid(), title = "Chris's birthday")
        val n2 = op2.applyOp(tree) { s -> console.log(s) }
        val cbOutlineNode = outline.addChild(n2, 1, helper)
        val op3 = AddNode(parent = n2.payload.id, node = newGuid(), title = "Figure out plans for Chris's birthday")
        val n3 = op3.applyOp(tree) { s -> console.log(s) }
        cbOutlineNode.addChild(n3, 0, helper)
        emit(op1, op2, op3)
        return tree
    }
}