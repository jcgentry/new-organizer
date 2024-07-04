package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
sealed interface Operation {
    val id: Int?

    // TODO Go through an enum?
    val opType: String
}

sealed interface OperationImpl<R> : Operation {
    fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): R

}

@Serializable
data class AddNode(
    override val id: Int? = null,  // Ugly that this is nullable
    val parent: Guid?,
    val node: Guid,
    val title: String,
    val index: Int? = null,
) : OperationImpl<Node<Task>> {
    override val opType = "AddNode"
    override fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): Node<Task> {
        if (parent == null) {
            tree.root = Node(Task(node, title), parent = null, tree = tree)
            return tree.root!!
        } else {
            val parentNode = tree.getNode { t -> t.id == parent }
            val newNode = Node(Task(node, title), parentNode, tree = tree)
            if (index == null)
                parentNode.children.add(newNode)
            else {
                parentNode.children.add(index, newNode)
            }
            return newNode
        }
    }
}

@Serializable
data class UpdateTitle(
    override val id: Int? = null,
    val node: Guid,
    val oldTitle: String,
    val title: String
) : OperationImpl<Node<Task>> {
    override val opType = "UpdateTitle"

    override fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): Node<Task> {
        val node = tree.getNode { n ->
            n.id == node
        }
        node.payload.title = title
        return node
    }
}