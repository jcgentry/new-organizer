package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
sealed interface Operation<R> {
    fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): R
}

@Serializable
data class AddNode(
    val parent: Guid?,
    val newNode: Guid,
    val title: String,
    val index: Int? = null,
) : Operation<Node<Task>> {
    override fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): Node<Task> {
        if (parent == null) {
            tree.root = Node(Task(newNode, title), parent = null, tree = tree)
            return tree.root!!
        } else {
            val parentNode = tree.getNode { t -> t.id == parent }
            val newNode = Node(Task(newNode, title), parentNode, tree = tree)
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
    val node: Guid,
    val oldTitle: String,
    val newTitle: String
) : Operation<Node<Task>> {
    override fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): Node<Task> {
        val node = tree.getNode { n ->
            n.id == node
        }
        node.payload.title = newTitle
        return node
    }
}