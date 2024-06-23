package com.jacagen.organizer

import kotlinx.serialization.Serializable
import kotlin.math.log

@Serializable
sealed interface Operation<R> {
    fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): R
}


@Serializable
data class AddNode(
    val parent: Guid?,
    val newNode: Guid,
    val label: String,
    val index: Int? = null,
) : Operation<Node<Task>> {
    override fun apply(tree: Tree<Task>, logger: ((String) -> Unit)): Node<Task> {
        if (parent == null) {
            tree.root = Node(Task(newGuid(), "\$root"), parent = null, tree = tree)
            return tree.root!!
        } else {
            val parentNode = tree[{ t -> t.id == parent }]
            val newNode = Node(Task(newNode, label), parentNode, tree = tree)
            if (index == null)
                parentNode.children.add(newNode)
            else {
                if (logger != null)
                    logger("Adding node at index ${index}")

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
        logger("Appling update title to $node")
        val node = tree.get { n ->
            logger("Finding node with id $node")
            n.id == node
        }
        node.payload.title = newTitle
        return node
    }
}